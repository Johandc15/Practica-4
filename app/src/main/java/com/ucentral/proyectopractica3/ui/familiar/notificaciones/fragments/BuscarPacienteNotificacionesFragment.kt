package com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado

class BuscarPacienteNotificacionesFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // UI
    private lateinit var tvPacienteActual: TextView
    private lateinit var spinnerPacientes: Spinner
    private lateinit var btnUsarPaciente: Button
    private lateinit var tvInfo: TextView

    // Listas en memoria
    private var pacientesIds: List<String> = emptyList()
    private var pacientesNombres: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_buscar_paciente_notificaciones,
            container,
            false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Referencias del layout (usaremos nuevos IDs en el XML)
        tvPacienteActual = view.findViewById(R.id.tvPacienteActualNotificaciones)
        spinnerPacientes = view.findViewById(R.id.spinnerPacientesAsociadosNotificaciones)
        btnUsarPaciente = view.findViewById(R.id.btnUsarPacienteNotificaciones)
        tvInfo = view.findViewById(R.id.tvInfoPacientesNotificaciones)

        // Mostrar paciente actual si ya hay uno
        mostrarPacienteActual()

        // Cargar pacientes asociados del familiar
        cargarPacientesAsociados()

        // Botón "Usar este paciente"
        btnUsarPaciente.setOnClickListener {
            seleccionarPacienteDeSpinner()
        }

        return view
    }

    /** Muestra el texto con el paciente actualmente seleccionado */
    private fun mostrarPacienteActual() {
        val nombre = PacienteSeleccionado.pacienteNombre
        tvPacienteActual.text = if (!nombre.isNullOrBlank()) {
            "Paciente actual: $nombre"
        } else {
            "Paciente actual: (sin seleccionar)"
        }
    }

    /** Lee familiares/{familiarId}/pacientesAsociados y llena el spinner */
    private fun cargarPacientesAsociados() {
        val familiarId = auth.currentUser?.uid ?: return

        db.collection("familiares").document(familiarId)
            .collection("pacientesAsociados")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // No hay pacientes asociados todavía
                    spinnerPacientes.visibility = View.GONE
                    btnUsarPaciente.isEnabled = false
                    tvInfo.visibility = View.VISIBLE
                    tvInfo.text =
                        "Aún no tienes pacientes asociados.\n" +
                                "Ve a \"Pacientes asociados\" en el inicio para agregarlos."
                    return@addOnSuccessListener
                }

                spinnerPacientes.visibility = View.VISIBLE
                btnUsarPaciente.isEnabled = true
                tvInfo.visibility = View.GONE

                pacientesIds = result.map { it.id }
                pacientesNombres = result.map { it.getString("nombre") ?: "Sin nombre" }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    pacientesNombres
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }

                spinnerPacientes.adapter = adapter

                // Si ya había un paciente seleccionado, marcarlo
                val actualId = PacienteSeleccionado.pacienteId
                if (!actualId.isNullOrBlank()) {
                    val index = pacientesIds.indexOf(actualId)
                    if (index >= 0) spinnerPacientes.setSelection(index)
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar pacientes asociados",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    /** Guarda en PacienteSeleccionado el paciente elegido en el spinner */
    private fun seleccionarPacienteDeSpinner() {
        if (pacientesIds.isEmpty()) return

        val pos = spinnerPacientes.selectedItemPosition
        if (pos < 0 || pos >= pacientesIds.size) return

        val id = pacientesIds[pos]
        val nombre = pacientesNombres[pos]

        PacienteSeleccionado.pacienteId = id
        PacienteSeleccionado.pacienteNombre = nombre

        mostrarPacienteActual()

        Toast.makeText(
            requireContext(),
            "Paciente seleccionado: $nombre",
            Toast.LENGTH_SHORT
        ).show()
    }
}
