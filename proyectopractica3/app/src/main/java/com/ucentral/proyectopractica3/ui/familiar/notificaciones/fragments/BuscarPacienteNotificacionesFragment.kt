package com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado

class BuscarPacienteNotificacionesFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etCorreoPaciente: EditText
    private lateinit var btnBuscar: Button
    private lateinit var tvResultado: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_buscar_paciente_notificaciones, container, false)

        db = FirebaseFirestore.getInstance()

        etCorreoPaciente = view.findViewById(R.id.etCorreoPacienteBuscar)
        btnBuscar = view.findViewById(R.id.btnBuscarPaciente)
        tvResultado = view.findViewById(R.id.tvResultadoBusqueda)

        btnBuscar.setOnClickListener {
            val correo = etCorreoPaciente.text.toString().trim()

            if (correo.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese un correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            buscarPaciente(correo)
        }

        return view
    }

    private fun buscarPaciente(correo: String) {
        db.collection("usuarios")
            .whereEqualTo("correo", correo)   // CORREGIDO AQUÍ
            .whereEqualTo("tipoUsuario", "Paciente") // Aseguramos que sea paciente
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    tvResultado.text = "❌ Paciente no encontrado."
                    PacienteSeleccionado.limpiar()
                } else {
                    val paciente = documents.first()
                    val nombre = paciente.getString("nombre") ?: "Sin nombre"
                    tvResultado.text = "✅ Paciente seleccionado: $nombre"
                    PacienteSeleccionado.pacienteId = paciente.id
                    PacienteSeleccionado.pacienteNombre = nombre
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al buscar paciente", Toast.LENGTH_SHORT).show()
                PacienteSeleccionado.limpiar()
            }
    }
}
