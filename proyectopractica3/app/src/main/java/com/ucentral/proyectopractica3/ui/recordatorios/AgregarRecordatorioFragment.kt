package com.ucentral.proyectopractica3.ui.recordatorios

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmChannels
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmScheduler
import com.ucentral.proyectopractica3.ui.notificaciones.ExactAlarmHelper
import com.ucentral.proyectopractica3.ui.notificaciones.PermissionUtils
import java.util.Calendar
import java.util.UUID

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

        // Selector de hora
        btnHora.setOnClickListener {
            val ahora = Calendar.getInstance()
            val horaActual = ahora.get(Calendar.HOUR_OF_DAY)
            val minutoActual = ahora.get(Calendar.MINUTE)

            val picker = TimePickerDialog(
                requireContext(),
                { _, h, m ->
                    // guardamos "HH:mm"
                    horaSeleccionada = String.format("%02d:%02d", h, m)
                    btnHora.text = "Hora: $horaSeleccionada"
                },
                horaActual,
                minutoActual,
                true // formato 24h
            )

            picker.show()
        }

        // Botón guardar recordatorio
        btnGuardar.setOnClickListener {
            val posSeleccionado = spinner.selectedItemPosition
            val repeticiones = etRepeticiones.text.toString().toIntOrNull() ?: 1

            if (horaSeleccionada.isEmpty() || medicamentos.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val med = medicamentos[posSeleccionado]
            val uid = auth.currentUser?.uid ?: return@setOnClickListener

            // Construimos el objeto que vas a guardar en Firestore
            val recordatorio = Recordatorio(
                id = UUID.randomUUID().toString(),
                medicamentoId = med.id,
                nombreMedicamento = med.nombre,
                hora = horaSeleccionada,
                repeticiones = repeticiones
            )

            // 1. Guardar en Firestore
            db.collection("usuarios").document(uid)
                .collection("recordatorios").document(recordatorio.id)
                .set(recordatorio)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Recordatorio guardado", Toast.LENGTH_SHORT).show()

                    // 2. Asegurar permisos / canal antes de programar la alarma
                    AlarmChannels.ensureCreated(requireContext())
                    PermissionUtils.ensurePostNotificationPermission(requireActivity())
                    ExactAlarmHelper.ensureExactAlarmAllowed(requireContext())

                    // 3. Programar la alarma intrusiva usando AlarmScheduler central
                    val (h, m) = recordatorio.hora.split(":").map { it.toInt() }

                    AlarmScheduler.scheduleDailyExact(
                        context = requireContext(),
                        recordatorioId = recordatorio.id,
                        nombreMedicamento = recordatorio.nombreMedicamento,
                        hora24 = h,
                        minuto = m
                    )

                    // 4. Limpiar la UI para que quede lista para agregar otro
                    etRepeticiones.text.clear()
                    btnHora.text = "Seleccionar hora"
                    horaSeleccionada = ""
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }

    private fun cargarMedicamentos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid)
            .collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                medicamentos = result.map { it.toObject(Medicamento::class.java) }

                val nombres = medicamentos.map { it.nombre }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    nombres
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar medicamentos", Toast.LENGTH_SHORT).show()
            }
    }
}
