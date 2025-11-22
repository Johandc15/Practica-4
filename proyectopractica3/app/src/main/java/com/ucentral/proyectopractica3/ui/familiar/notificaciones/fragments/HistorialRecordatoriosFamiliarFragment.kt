package com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado

class HistorialRecordatoriosFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var tvResumenHistorial: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_historial_recordatorios_familiar, container, false)

        tvResumenHistorial = view.findViewById(R.id.tvResumenHistorialFamiliar)
        db = FirebaseFirestore.getInstance()

        cargarHistorial()

        return view
    }

    private fun cargarHistorial() {
        val pacienteId = PacienteSeleccionado.pacienteId ?: return
        db.collection("usuarios").document(pacienteId)
            .collection("recordatorios")
            .get()
            .addOnSuccessListener { result ->
                var total = 0
                var tomados = 0

                for (doc in result) {
                    val rec = doc.toObject(Recordatorio::class.java)
                    total += rec.repeticiones
                    if (rec.tomado) tomados += rec.repeticiones
                }

                val porcentaje = if (total == 0) 0 else (tomados * 100 / total)

                tvResumenHistorial.text = """
                    Total programados: $total
                    Tomados: $tomados
                    Porcentaje cumplimiento: $porcentaje%
                """.trimIndent()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al calcular historial", Toast.LENGTH_SHORT).show()
            }
    }
}
