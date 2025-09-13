package com.ucentral.proyectopractica3.ui.familiar.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R

class ListaPacientesAsociadosFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var layoutPacientes: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_lista_pacientes_asociados, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        layoutPacientes = view.findViewById(R.id.layoutListaPacientes)

        cargarPacientes()

        return view
    }

    private fun cargarPacientes() {
        val familiarId = auth.currentUser?.uid ?: return
        db.collection("familiares").document(familiarId)
            .collection("pacientesAsociados")
            .get()
            .addOnSuccessListener { result ->
                layoutPacientes.removeAllViews()

                for (doc in result) {
                    val pacienteNombre = doc.getString("nombre") ?: "Paciente sin nombre"
                    val pacienteCorreo = doc.getString("correo") ?: "Correo no disponible"

                    agregarPacienteExpandible(pacienteNombre, pacienteCorreo)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar pacientes", Toast.LENGTH_SHORT).show()
            }
    }

    private fun agregarPacienteExpandible(nombre: String, correo: String) {
        val contexto = requireContext()

        val contenedor = LinearLayout(contexto).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16)
            setBackgroundResource(R.drawable.border_gray)  // Usa el mismo estilo bonito que en medicamentos
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 24)
            layoutParams = params
        }

        val titulo = TextView(contexto).apply {
            text = nombre
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setTextColor(resources.getColor(R.color.purple_700, null))
        }

        val detalles = TextView(contexto).apply {
            text = "Correo: $correo"
            textSize = 16f
            visibility = View.GONE
        }

        titulo.setOnClickListener {
            detalles.visibility = if (detalles.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        contenedor.addView(titulo)
        contenedor.addView(detalles)
        layoutPacientes.addView(contenedor)
    }
}
