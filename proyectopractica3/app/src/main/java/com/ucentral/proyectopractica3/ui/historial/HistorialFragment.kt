package com.ucentral.proyectopractica3.ui.historial

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.HistorialItem
import com.ucentral.proyectopractica3.model.Recordatorio

class HistorialFragment : Fragment() {

    private lateinit var layoutHistorial: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)
        layoutHistorial = view.findViewById(R.id.layoutHistorial)
        cargarDatos()
        return view
    }

    private fun cargarDatos() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid)
            .collection("recordatorios")
            .get()
            .addOnSuccessListener { result ->
                val mapa = mutableMapOf<String, MutableList<Recordatorio>>()

                for (doc in result) {
                    val rec = doc.toObject(Recordatorio::class.java)
                    if (!mapa.containsKey(rec.nombreMedicamento)) {
                        mapa[rec.nombreMedicamento] = mutableListOf()
                    }
                    mapa[rec.nombreMedicamento]?.add(rec)
                }

                layoutHistorial.removeAllViews()

                for ((nombre, lista) in mapa) {
                    val total = lista.size
                    val tomados = lista.count { it.tomado }
                    val porcentaje = if (total > 0) (tomados * 100) / total else 0
                    val horas = lista.filter { it.tomado }.map { it.hora }

                    val item = HistorialItem(nombre, total, tomados, porcentaje, horas)
                    mostrarItem(item)
                }
            }
    }

    private fun mostrarItem(item: HistorialItem) {
        val contexto = requireContext()
        val contenedor = LinearLayout(contexto).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24)
            setBackgroundResource(R.drawable.border_gray)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, 24) }
        }

        val titulo = TextView(contexto).apply {
            text = "📌 ${item.nombreMedicamento}"
            textSize = 18f
        }

        val resumen = TextView(contexto).apply {
            text = """
                ✅ Tomados: ${item.tomados}/${item.totalRecordatorios}
                📊 Cumplimiento: ${item.porcentaje}%
                🕒 Horas: ${item.horasTomadas.joinToString(", ")}
            """.trimIndent()
        }

        contenedor.addView(titulo)
        contenedor.addView(resumen)
        layoutHistorial.addView(contenedor)
    }
}
