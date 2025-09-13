package com.ucentral.proyectopractica3.ui.familiar.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BuscarPacienteFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var etCorreoPaciente: EditText
    private lateinit var btnBuscarPaciente: Button
    private lateinit var tvResultado: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_buscar_paciente, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etCorreoPaciente = view.findViewById(R.id.etCorreoPaciente)
        btnBuscarPaciente = view.findViewById(R.id.btnBuscarPaciente)
        tvResultado = view.findViewById(R.id.tvResultadoPaciente)

        btnBuscarPaciente.setOnClickListener {
            val correo = etCorreoPaciente.text.toString().trim()
            if (correo.isNotEmpty()) {
                buscarPaciente(correo)
            }
        }

        return view
    }

    private fun buscarPaciente(correo: String) {
        db.collection("usuarios")
            .whereEqualTo("correo", correo)
            .whereEqualTo("tipoUsuario", "Paciente")
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val paciente = result.documents[0]
                    PacienteSeleccionado.pacienteId = paciente.id
                    PacienteSeleccionado.pacienteNombre = paciente.getString("nombre") ?: ""
                    tvResultado.text = "✅ Paciente seleccionado: ${PacienteSeleccionado.pacienteNombre}"
                } else {
                    PacienteSeleccionado.limpiar()
                    tvResultado.text = "❌ Paciente no encontrado."
                }
            }
            .addOnFailureListener {
                PacienteSeleccionado.limpiar()
                tvResultado.text = "❌ Error al buscar paciente."
            }
    }
}
