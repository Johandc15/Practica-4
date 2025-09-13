package com.ucentral.proyectopractica3.ui.recordatorios

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Recordatorio
import java.text.SimpleDateFormat
import java.util.*

class NotificacionesFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var layoutNotificaciones: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_notificaciones, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        layoutNotificaciones = view.findViewById(R.id.layoutNotificaciones)

        cargarNotificaciones()

        return view
    }

    private fun cargarNotificaciones() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid)
            .collection("recordatorios")
            .get()
            .addOnSuccessListener { result ->
                layoutNotificaciones.removeAllViews()
                val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
                val ahora = formato.format(Date())

                for (doc in result) {
                    val rec = doc.toObject(Recordatorio::class.java)
                    val container = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(24, 16, 24, 16)
                        setBackgroundResource(R.drawable.border_gray)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply { setMargins(0, 0, 0, 24) }
                    }

                    val texto = TextView(requireContext()).apply {
                        text = "Medicamento: ${rec.nombreMedicamento}\nHora: ${rec.hora} | Veces: ${rec.repeticiones}"
                        textSize = 16f
                    }

                    val btnTomar = Button(requireContext()).apply {
                        text = "Tomar"
                        setOnClickListener {
                            db.collection("usuarios").document(uid)
                                .collection("recordatorios").document(rec.id)
                                .update("tomado", true)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "✅ Tomado", Toast.LENGTH_SHORT).show()
                                    cargarNotificaciones()
                                }
                        }
                    }

                    val btnPosponer = Button(requireContext()).apply {
                        text = "Posponer"
                        setOnClickListener {
                            Toast.makeText(requireContext(), "⏰ Pospuesto por 10 min", Toast.LENGTH_SHORT).show()
                            // Aquí podrías reprogramar una notificación o simplemente no hacer nada.
                        }
                    }

                    container.addView(texto)
                    container.addView(btnTomar)
                    container.addView(btnPosponer)
                    layoutNotificaciones.addView(container)
                }
            }
    }
}
