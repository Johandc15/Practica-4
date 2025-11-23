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
    private lateinit var tvPacienteActual: TextView

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

        // Referencias UI
        tvPacienteActual = view.findViewById(R.id.tvPacienteActualRecordatorioFamiliar)
        spinnerMedicamentos = view.findViewById(R.id.spinnerMedicamentosFamiliar)
        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHoraFamiliar)
        etRepeticiones = view.findViewById(R.id.etRepeticionesFamiliar)
        btnGuardar = view.findViewById(R.id.btnGuardarRecordatorioFamiliar)

        // Estado inicial de encabezado y UI
        actualizarEncabezadoPaciente()
        actualizarEstadoUI(segunPaciente = PacienteSeleccionado.pacienteId != null)

        // Cargar medicamentos (si hay paciente)
        cargarMedicamentos()

        btnSeleccionarHora.setOnClickListener { mostrarTimePicker() }
        btnGuardar.setOnClickListener { guardarRecordatorio() }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Si el usuario cambió el paciente en otra pantalla, refrescamos todo al volver
        actualizarEncabezadoPaciente()
        actualizarEstadoUI(segunPaciente = PacienteSeleccionado.pacienteId != null)
        cargarMedicamentos()
    }

    private fun actualizarEncabezadoPaciente() {
        val nombrePaciente = PacienteSeleccionado.pacienteNombre
        tvPacienteActual.text = if (!nombrePaciente.isNullOrBlank()) {
            "Paciente actual: $nombrePaciente"
        } else {
            "Paciente actual: (sin seleccionar)"
        }
    }

    private fun actualizarEstadoUI(segunPaciente: Boolean) {
        spinnerMedicamentos.isEnabled = segunPaciente
        btnSeleccionarHora.isEnabled = segunPaciente
        etRepeticiones.isEnabled = segunPaciente
        btnGuardar.isEnabled = segunPaciente
    }

    private fun cargarMedicamentos() {
        val pacienteId = PacienteSeleccionado.pacienteId
        if (pacienteId == null) {
            // Sin paciente: dejamos spinner vacío y deshabilitado
            medicamentos = emptyList()
            val adapterVacio = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listOf("— Selecciona un paciente primero —")
            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            spinnerMedicamentos.adapter = adapterVacio

            Toast.makeText(
                requireContext(),
                "Selecciona primero un paciente desde 'Pacientes asociados'.",
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
                    val adapterVacio = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        listOf("— Sin medicamentos registrados —")
                    ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    spinnerMedicamentos.adapter = adapterVacio

                    Toast.makeText(
                        requireContext(),
                        "Este paciente no tiene medicamentos registrados.",
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
                    "Error al cargar medicamentos.",
                    Toast.LENGTH_SHORT
                ).show()
                // Dejar un estado seguro en el spinner
                val adapterError = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    listOf("— Error al cargar —")
                ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                spinnerMedicamentos.adapter = adapterError
                medicamentos = emptyList()
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
                "Selecciona primero un paciente.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (medicamentos.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "No hay medicamentos disponibles para este paciente.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (horaSeleccionada.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Selecciona una hora para el recordatorio.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val repeticiones = etRepeticiones.text.toString().toIntOrNull() ?: 1
        if (repeticiones <= 0) {
            Toast.makeText(
                requireContext(),
                "Las repeticiones deben ser al menos 1.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val seleccionado = spinnerMedicamentos.selectedItemPosition
        if (seleccionado < 0 || seleccionado >= medicamentos.size) {
            Toast.makeText(
                requireContext(),
                "Selecciona un medicamento válido.",
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
            // (Otros campos de Recordatorio permanecen con valores por defecto)
        )

        db.collection("usuarios").document(pacienteId)
            .collection("recordatorios")
            .document(recordatorio.id)
            .set(recordatorio)
            .addOnSuccessListener {
                val nombrePaciente = PacienteSeleccionado.pacienteNombre
                Toast.makeText(
                    requireContext(),
                    if (!nombrePaciente.isNullOrBlank())
                        "Recordatorio guardado para $nombrePaciente."
                    else
                        "Recordatorio guardado.",
                    Toast.LENGTH_SHORT
                ).show()

                // Limpiar formulario
                etRepeticiones.text.clear()
                horaSeleccionada = ""
                btnSeleccionarHora.text = "Seleccionar hora"
                if (spinnerMedicamentos.adapter != null && spinnerMedicamentos.adapter.count > 0) {
                    spinnerMedicamentos.setSelection(0)
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error al guardar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
