package com.ucentral.proyectopractica3.ui.familiar.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R

class AgregarPacienteFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var etCorreoPaciente: EditText
    private lateinit var btnAsociar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_agregar_paciente, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etCorreoPaciente = view.findViewById(R.id.etCorreoPaciente)
        btnAsociar = view.findViewById(R.id.btnAsociarPaciente)

        btnAsociar.setOnClickListener {
            val correoPaciente = etCorreoPaciente.text.toString().trim().lowercase()

            if (correoPaciente.isNotEmpty()) {
                buscarYAsociarPaciente(correoPaciente)
            } else {
                Toast.makeText(requireContext(), "Ingresa el correo del paciente", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun buscarYAsociarPaciente(correo: String) {
        db.collection("usuarios")
            .whereEqualTo("correo", correo) // Buscar por correo exacto (todo en minúscula)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val paciente = result.documents[0]

                    val tipoUsuario = paciente.getString("tipoUsuario")
                    if (tipoUsuario != "Paciente") {
                        Toast.makeText(requireContext(), "El usuario no es un paciente", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val familiarId = auth.currentUser?.uid ?: return@addOnSuccessListener
                    val pacienteId = paciente.id

                    db.collection("familiares").document(familiarId)
                        .collection("pacientesAsociados")
                        .document(pacienteId)
                        .set(mapOf(
                            "nombre" to (paciente.getString("nombre") ?: "Sin Nombre"),
                            "correo" to (paciente.getString("correo") ?: "Sin Correo")
                        ))
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Paciente asociado correctamente", Toast.LENGTH_SHORT).show()
                            etCorreoPaciente.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error al asociar paciente", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Paciente no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al buscar paciente", Toast.LENGTH_SHORT).show()
            }
    }
}
