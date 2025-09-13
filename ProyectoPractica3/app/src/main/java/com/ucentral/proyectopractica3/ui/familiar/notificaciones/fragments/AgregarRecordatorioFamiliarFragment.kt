package com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import java.util.*

class AgregarRecordatorioFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var spinnerMedicamentos: Spinner
    private lateinit var btnSeleccionarHora: Button
    private lateinit var etRepeticiones: EditText
    private lateinit var btnGuardar: Button

    private var horaSeleccionada: String = ""
    private var medicamentos: List<Medicamento> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_agregar_recordatorio_familiar, container, false)

        db = FirebaseFirestore.getInstance()

        spinnerMedicamentos = view.findViewById(R.id.spinnerMedicamentosFamiliar)
        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHoraFamiliar)
        etRepeticiones = view.findViewById(R.id.etRepeticionesFamiliar)
        btnGuardar = view.findViewById(R.id.btnGuardarRecordatorioFamiliar)

        cargarMedicamentos()

        btnSeleccionarHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val picker = TimePickerDialog(requireContext(), { _, hour, minute ->
                horaSeleccionada = String.format("%02d:%02d", hour, minute)
                btnSeleccionarHora.text = "Hora: $horaSeleccionada"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            picker.show()
        }

        btnGuardar.setOnClickListener {
            guardarRecordatorio()
        }

        return view
    }

    private fun cargarMedicamentos() {
        val pacienteId = PacienteSeleccionado.pacienteId ?: return
        db.collection("usuarios").document(pacienteId).collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                medicamentos = result.map { it.toObject(Medicamento::class.java) }
                val nombres = medicamentos.map { it.nombre }
                spinnerMedicamentos.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar medicamentos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun guardarRecordatorio() {
        val pacienteId = PacienteSeleccionado.pacienteId
        val seleccionado = spinnerMedicamentos.selectedItemPosition
        val repeticiones = etRepeticiones.text.toString().toIntOrNull() ?: 1

        if (pacienteId == null || horaSeleccionada.isEmpty() || medicamentos.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val medicamentoSeleccionado = medicamentos[seleccionado]

        val recordatorio = Recordatorio(
            id = UUID.randomUUID().toString(),
            medicamentoId = medicamentoSeleccionado.id,
            nombreMedicamento = medicamentoSeleccionado.nombre,
            hora = horaSeleccionada,
            repeticiones = repeticiones
        )

        db.collection("usuarios").document(pacienteId)
            .collection("recordatorios")
            .document(recordatorio.id)
            .set(recordatorio)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Recordatorio guardado", Toast.LENGTH_SHORT).show()
                etRepeticiones.text.clear()
                btnSeleccionarHora.text = "Seleccionar hora"
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }
}
