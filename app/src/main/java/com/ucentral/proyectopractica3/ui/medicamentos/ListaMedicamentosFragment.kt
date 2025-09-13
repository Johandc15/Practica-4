package com.ucentral.proyectopractica3.ui.medicamentos

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento

class ListaMedicamentosFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var layoutLista: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_lista_medicamentos, container, false)
        layoutLista = view.findViewById(R.id.layoutListaMedicamentos)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        cargarMedicamentos()

        return view
    }

    private fun cargarMedicamentos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid)
            .collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                layoutLista.removeAllViews()

                for (document in result) {
                    val med = document.toObject(Medicamento::class.java)
                    agregarMedicamentoExpandible(med)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar medicamentos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun agregarMedicamentoExpandible(med: Medicamento) {
        val contexto = requireContext()

        val contenedor = LinearLayout(contexto).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16)
            setBackgroundResource(R.drawable.border_gray)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 24)
            layoutParams = params
        }

        val titulo = TextView(contexto).apply {
            text = med.nombre
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setTextColor(resources.getColor(R.color.purple_700, null))
        }

        val detalles = TextView(contexto).apply {
            text = "Dosis: ${med.dosis}\nCantidad: ${med.cantidad}\nHorario: ${med.horario}\nNotas: ${med.notas}"
            visibility = View.GONE
        }

        titulo.setOnClickListener {
            detalles.visibility = if (detalles.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        contenedor.addView(titulo)
        contenedor.addView(detalles)
        layoutLista.addView(contenedor)
    }
}
