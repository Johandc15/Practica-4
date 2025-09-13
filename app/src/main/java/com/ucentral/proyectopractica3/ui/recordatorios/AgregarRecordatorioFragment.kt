package com.ucentral.proyectopractica3.ui.recordatorios

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmaReceiver
import java.util.*

class AgregarRecordatorioFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var spinner: Spinner
    private lateinit var btnHora: Button
    private lateinit var etRepeticiones: EditText
    private lateinit var btnGuardar: Button

    private var horaSeleccionada: String = ""
    private var medicamentos: List<Medicamento> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_agregar_recordatorio, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        spinner = view.findViewById(R.id.spinnerMedicamento)
        btnHora = view.findViewById(R.id.btnHora)
        etRepeticiones = view.findViewById(R.id.etRepeticiones)
        btnGuardar = view.findViewById(R.id.btnGuardarRecordatorio)

        cargarMedicamentos()

        btnHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hora = calendar.get(Calendar.HOUR_OF_DAY)
            val minuto = calendar.get(Calendar.MINUTE)

            val picker = TimePickerDialog(requireContext(), { _, h, m ->
                horaSeleccionada = String.format("%02d:%02d", h, m)
                btnHora.text = "Hora: $horaSeleccionada"
            }, hora, minuto, true)

            picker.show()
        }

        btnGuardar.setOnClickListener {
            val seleccionado = spinner.selectedItemPosition
            val repeticiones = etRepeticiones.text.toString().toIntOrNull() ?: 1

            if (horaSeleccionada.isEmpty() || medicamentos.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val med = medicamentos[seleccionado]
            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            val recordatorio = Recordatorio(
                id = UUID.randomUUID().toString(),
                medicamentoId = med.id,
                nombreMedicamento = med.nombre,
                hora = horaSeleccionada,
                repeticiones = repeticiones
            )

            db.collection("usuarios").document(uid)
                .collection("recordatorios").document(recordatorio.id)
                .set(recordatorio)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Recordatorio guardado", Toast.LENGTH_SHORT).show()

                    // 🔔 Aquí programas la alarma
                    val alarmIntent = Intent(requireContext(), AlarmaReceiver::class.java).apply {
                        putExtra("idRecordatorio", recordatorio.id)
                        putExtra("nombreMedicamento", recordatorio.nombreMedicamento)
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        requireContext(), recordatorio.id.hashCode(), alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val parts = recordatorio.hora.split(":")
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, parts[0].toInt())
                        set(Calendar.MINUTE, parts[1].toInt())
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }

                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }

    private fun cargarMedicamentos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid).collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                medicamentos = result.map { it.toObject(Medicamento::class.java) }
                val nombres = medicamentos.map { it.nombre }
                spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar medicamentos", Toast.LENGTH_SHORT).show()
            }
    }
}
