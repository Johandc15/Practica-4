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
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado

class ListaPacientesAsociadosFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var layoutPacientes: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_lista_pacientes_asociados,
            container,
            false
        )

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

                if (result.isEmpty) {
                    // Mensaje cuando aún no hay pacientes asociados
                    val mensaje = TextView(requireContext()).apply {
                        text = "Aún no tienes pacientes asociados.\n" +
                                "Usa la opción \"Asociar paciente\" para agregar uno."
                        textSize = 16f
                        setPadding(16)
                    }
                    layoutPacientes.addView(mensaje)
                    return@addOnSuccessListener
                }

                for (doc in result) {
                    val pacienteId = doc.id
                    val pacienteNombre = doc.getString("nombre") ?: "Paciente sin nombre"
                    val pacienteCorreo = doc.getString("correo") ?: "Correo no disponible"

                    agregarPacienteExpandible(pacienteId, pacienteNombre, pacienteCorreo)
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar pacientes",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun agregarPacienteExpandible(
        pacienteId: String,
        nombre: String,
        correo: String
    ) {
        val contexto = requireContext()

        val contenedor = LinearLayout(contexto).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16)
            setBackgroundResource(R.drawable.border_gray)  // Usa el mismo estilo que ya tenías
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 24)
            layoutParams = params
        }

        // Título con el nombre del paciente
        val titulo = TextView(contexto).apply {
            text = nombre
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setTextColor(resources.getColor(R.color.purple_700, null))
        }

        // Texto con detalles (correo), expandible
        val detalles = TextView(contexto).apply {
            text = "Correo: $correo"
            textSize = 16f
            visibility = View.GONE
        }

        // Etiqueta para indicar si este paciente está seleccionado actualmente
        val labelSeleccionado = TextView(contexto).apply {
            text = if (PacienteSeleccionado.pacienteId == pacienteId)
                "✅ Paciente actualmente seleccionado"
            else
                ""
            textSize = 14f
        }

        // Botón para usar este paciente como "paciente actual"
        val btnSeleccionar = Button(contexto).apply {
            text = if (PacienteSeleccionado.pacienteId == pacienteId)
                "Usando este paciente"
            else
                "Usar este paciente"

            isAllCaps = false

            setOnClickListener {
                // Guardamos globalmente qué paciente está usando este familiar
                PacienteSeleccionado.pacienteId = pacienteId
                PacienteSeleccionado.pacienteNombre = nombre   // 🔹 IMPORTANTE: guardamos también el nombre

                Toast.makeText(
                    contexto,
                    "Paciente seleccionado: $nombre",
                    Toast.LENGTH_SHORT
                ).show()

                // Recargar la lista para refrescar textos y marcar el actual
                cargarPacientes()
            }
        }

        // Al tocar el título, mostramos / ocultamos el correo (comportamiento que ya tenías)
        titulo.setOnClickListener {
            detalles.visibility =
                if (detalles.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        contenedor.addView(titulo)
        contenedor.addView(detalles)
        contenedor.addView(labelSeleccionado)
        contenedor.addView(btnSeleccionar)

        layoutPacientes.addView(contenedor)
    }
}
