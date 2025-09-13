package com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado

class ListaRecordatoriosFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var layout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_lista_recordatorios_familiar, container, false)

        layout = view.findViewById(R.id.layoutListaRecordatoriosFamiliar)
        db = FirebaseFirestore.getInstance()

        cargarRecordatorios()

        return view
    }

    private fun cargarRecordatorios() {
        val pacienteId = PacienteSeleccionado.pacienteId ?: return
        db.collection("usuarios").document(pacienteId)
            .collection("recordatorios")
            .get()
            .addOnSuccessListener { result ->
                layout.removeAllViews()
                for (doc in result) {
                    val rec = doc.toObject(Recordatorio::class.java)

                    val container = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(24, 16, 24, 16)
                        setBackgroundResource(R.drawable.border_gray)
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 0, 0, 24)
                        layoutParams = params
                    }

                    val texto = TextView(requireContext()).apply {
                        text = "Hora: ${rec.hora}\nRepeticiones: ${rec.repeticiones}\nEstado: ${if (rec.tomado) "✅ Tomado" else "⏳ Pendiente"}"
                        textSize = 16f
                    }

                    container.addView(texto)
                    layout.addView(container)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar recordatorios", Toast.LENGTH_SHORT).show()
            }
    }
}
