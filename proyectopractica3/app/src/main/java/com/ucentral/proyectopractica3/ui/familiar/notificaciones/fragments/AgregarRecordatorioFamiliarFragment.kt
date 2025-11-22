package com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import java.util.Calendar
import java.util.UUID

class AgregarRecordatorioFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var spinnerMedicamentos: Spinner
    private lateinit var btnSeleccionarHora: Button
    private lateinit var etRepeticiones: EditText
    private lateinit var btnGuardar: Button

    private var horaSeleccionada: String = ""
    private var medicamentos: List<Medicamento> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_agregar_recordatorio_familiar,
            container,
            false
        )

        db = FirebaseFirestore.getInstance()

        spinnerMedicamentos = view.findViewById(R.id.spinnerMedicamentosFamiliar)
        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHoraFamiliar)
        etRepeticiones = view.findViewById(R.id.etRepeticionesFamiliar)
        btnGuardar = view.findViewById(R.id.btnGuardarRecordatorioFamiliar)

        cargarMedicamentos()

        btnSeleccionarHora.setOnClickListener {
            mostrarTimePicker()
        }

        btnGuardar.setOnClickListener {
            guardarRecordatorio()
        }

        return view
    }

    private fun cargarMedicamentos() {
        val pacienteId = PacienteSeleccionado.pacienteId
        if (pacienteId == null) {
            Toast.makeText(
                requireContext(),
                "Selecciona primero un paciente",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        db.collection("usuarios").document(pacienteId)
            .collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                medicamentos = result.map { it.toObject(Medicamento::class.java) }

                if (medicamentos.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Este paciente no tiene medicamentos registrados",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }

                val nombres = medicamentos.map { it.nombre }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    nombres
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }

                spinnerMedicamentos.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar medicamentos",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun mostrarTimePicker() {
        val calendar = Calendar.getInstance()
        val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
        val minutoActual = calendar.get(Calendar.MINUTE)

        val picker = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                horaSeleccionada = String.format("%02d:%02d", hour, minute)
                btnSeleccionarHora.text = "Hora: $horaSeleccionada"
            },
            horaActual,
            minutoActual,
            true
        )
        picker.show()
    }

    private fun guardarRecordatorio() {
        val pacienteId = PacienteSeleccionado.pacienteId
        if (pacienteId == null) {
            Toast.makeText(
                requireContext(),
                "Selecciona primero un paciente",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (medicamentos.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "No hay medicamentos disponibles para este paciente",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (horaSeleccionada.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Selecciona una hora para el recordatorio",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val repeticiones = etRepeticiones.text.toString().toIntOrNull() ?: 1
        if (repeticiones <= 0) {
            Toast.makeText(
                requireContext(),
                "Las repeticiones deben ser al menos 1",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val seleccionado = spinnerMedicamentos.selectedItemPosition
        if (seleccionado < 0 || seleccionado >= medicamentos.size) {
            Toast.makeText(
                requireContext(),
                "Selecciona un medicamento válido",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val medicamentoSeleccionado = medicamentos[seleccionado]

        val recordatorioId = UUID.randomUUID().toString()

        val recordatorio = Recordatorio(
            id = recordatorioId,
            medicamentoId = medicamentoSeleccionado.id,
            nombreMedicamento = medicamentoSeleccionado.nombre,
            hora = horaSeleccionada,
            repeticiones = repeticiones
            // los demás campos de Recordatorio usan sus valores por defecto (ej: tomado = false)
        )

        db.collection("usuarios").document(pacienteId)
            .collection("recordatorios")
            .document(recordatorio.id)
            .set(recordatorio)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Recordatorio guardado",
                    Toast.LENGTH_SHORT
                ).show()

                // Limpiar formulario
                etRepeticiones.text.clear()
                horaSeleccionada = ""
                btnSeleccionarHora.text = "Seleccionar hora"
                spinnerMedicamentos.setSelection(0)
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error al guardar",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
