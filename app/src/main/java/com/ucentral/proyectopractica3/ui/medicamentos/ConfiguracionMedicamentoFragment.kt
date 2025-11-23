package com.ucentral.proyectopractica3.ui.medicamentos

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento

class ConfiguracionMedicamentoFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var layout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_configuracion_medicamento, container, false)
        layout = view.findViewById(R.id.layoutConfiguracion)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        cargarMedicamentos()

        return view
    }

    private fun cargarMedicamentos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid).collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                layout.removeAllViews()
                for (doc in result) {
                    val med = doc.toObject(Medicamento::class.java)
                    agregarVistaMedicamento(med, doc.id)
                }
            }
    }

    private fun agregarVistaMedicamento(med: Medicamento, id: String) {
        val contexto = requireContext()

        val contenedor = LinearLayout(contexto).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16)
            setBackgroundResource(R.drawable.border_gray)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
        }

        // ===== TEXTO MÁS PROFESIONAL =====
        val texto = TextView(contexto).apply {
            // Ciclos (si no hay nada en horario, mostramos "No configurado")
            val ciclos = if (med.horario.isBlank()) {
                "No configurado"
            } else {
                med.horario
            }

            // Rango de fechas (tratamiento definido vs crónico)
            val rango = if (med.usoCronico) {
                "Fecha de inicio: ${med.fechaInicio} · Uso crónico"
            } else {
                "Fecha de inicio: ${med.fechaInicio} · Fecha de terminación: ${med.fechaFin}"
            }

            text = """
                ${med.nombre}
                ${if (med.nombreGenerico.isNotBlank()) "(${med.nombreGenerico})" else ""}
                Dosis: ${med.dosis} | Cantidad total: ${med.cantidad}
                Ciclos: $ciclos
                $rango
            """.trimIndent()

            textSize = 14f
        }

        val btnEliminar = Button(contexto).apply {
            text = "Eliminar"
            setOnClickListener {
                val uid = auth.currentUser?.uid ?: return@setOnClickListener
                db.collection("usuarios").document(uid)
                    .collection("medicamentos").document(id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(contexto, "Eliminado", Toast.LENGTH_SHORT).show()
                        cargarMedicamentos()
                    }
                    .addOnFailureListener {
                        Toast.makeText(contexto, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        contenedor.addView(texto)
        contenedor.addView(btnEliminar)
        layout.addView(contenedor)
    }
}
