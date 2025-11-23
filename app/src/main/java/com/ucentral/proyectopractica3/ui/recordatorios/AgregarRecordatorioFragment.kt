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
import java.util.Calendar

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
        configurarPickerHora()
        configurarBotonGuardar()

        return view
    }

    // -------------------- UI --------------------
    private fun configurarPickerHora() {
        btnHora.setOnClickListener {
            val cal = Calendar.getInstance()
            val h = cal.get(Calendar.HOUR_OF_DAY)
            val m = cal.get(Calendar.MINUTE)

            TimePickerDialog(
                requireContext(),
                { _, hh, mm ->
                    horaSeleccionada = String.format("%02d:%02d", hh, mm)
                    btnHora.text = "Hora: $horaSeleccionada"
                },
                h, m, true
            ).show()
        }
    }

    private fun configurarBotonGuardar() {
        btnGuardar.setOnClickListener {
            val pos = spinner.selectedItemPosition
            val repeticiones = etRepeticiones.text.toString().toIntOrNull() ?: 1

            if (medicamentos.isEmpty()) {
                Toast.makeText(requireContext(), "No hay medicamentos cargados", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pos !in medicamentos.indices) {
                Toast.makeText(requireContext(), "Selecciona un medicamento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (horaSeleccionada.isEmpty()) {
                Toast.makeText(requireContext(), "Selecciona una hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (repeticiones <= 0) {
                Toast.makeText(requireContext(), "Las repeticiones deben ser ≥ 1", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val med = medicamentos[pos]
            val pacienteId = auth.currentUser?.uid // paciente actual (tú)

            Toast.makeText(requireContext(), "Abriendo planificador…", Toast.LENGTH_SHORT).show()

            // 👉 Integramos con el planificador avanzado, con el medicamento fijado.
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer, // contenedor de activity_paciente_medicamentos.xml
                    ProgramarRecordatoriosFragment.newInstance(
                        pacienteId = pacienteId,
                        medicamentoId = med.id
                    )
                )
                .addToBackStack("AgregarRecordatorio->Programar")
                .commitAllowingStateLoss()
        }
    }

    // -------------------- Datos --------------------
    private fun cargarMedicamentos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid)
            .collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                medicamentos = result.map { it.toObject(Medicamento::class.java) }

                if (medicamentos.isEmpty()) {
                    Toast.makeText(requireContext(), "Aún no tienes medicamentos registrados", Toast.LENGTH_SHORT).show()
                }

                val nombres = medicamentos.map { it.nombre }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    nombres
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                spinner.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar medicamentos", Toast.LENGTH_SHORT).show()
            }
    }
}
