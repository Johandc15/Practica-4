package com.ucentral.proyectopractica3.ui.familiar.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import com.google.firebase.firestore.FirebaseFirestore

class ListaMedicamentosFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var layoutLista: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_lista_medicamentos_familiar, container, false)

        db = FirebaseFirestore.getInstance()
        layoutLista = view.findViewById(R.id.layoutListaMedicamentosFamiliar)

        cargarMedicamentos()

        return view
    }

    private fun cargarMedicamentos() {
        val pacienteId = PacienteSeleccionado.pacienteId ?: return
        db.collection("usuarios").document(pacienteId)
            .collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                layoutLista.removeAllViews()

                for (doc in result) {
                    val med = doc.toObject(Medicamento::class.java)

                    val contenedor = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(16)
                        setBackgroundResource(R.drawable.border_gray)
                    }

                    val nombre = TextView(requireContext()).apply {
                        text = med.nombre
                        textSize = 18f
                        setTypeface(null, Typeface.BOLD)
                    }

                    val detalles = TextView(requireContext()).apply {
                        text = "Dosis: ${med.dosis}\nCantidad: ${med.cantidad}"
                        visibility = View.GONE
                    }

                    nombre.setOnClickListener {
                        detalles.visibility = if (detalles.visibility == View.GONE) View.VISIBLE else View.GONE
                    }

                    contenedor.addView(nombre)
                    contenedor.addView(detalles)
                    layoutLista.addView(contenedor)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar medicamentos", Toast.LENGTH_SHORT).show()
            }
    }
}
