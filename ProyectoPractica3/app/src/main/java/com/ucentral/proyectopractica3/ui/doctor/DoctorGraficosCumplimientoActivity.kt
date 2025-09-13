package com.ucentral.proyectopractica3.ui.doctor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.ui.DoctorHomeActivity

class DoctorGraficosCumplimientoActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var layoutCumplimiento: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_graficos_cumplimiento)

        db = FirebaseFirestore.getInstance()
        layoutCumplimiento = findViewById(R.id.layoutCumplimientoGeneral)

        findViewById<Button>(R.id.btnVolverGraficosCumplimiento).setOnClickListener {
            startActivity(Intent(this, DoctorHomeActivity::class.java))
            finish()
        }

        cargarCumplimientoGeneral()
    }

    private fun cargarCumplimientoGeneral() {
        db.collection("usuarios")
            .whereEqualTo("tipoUsuario", "Paciente")
            .get()
            .addOnSuccessListener { pacientes ->
                for (doc in pacientes) {
                    val id = doc.id
                    val nombre = doc.getString("nombre") ?: "Desconocido"

                    db.collection("usuarios").document(id)
                        .collection("recordatorios")
                        .get()
                        .addOnSuccessListener { recordatorios ->
                            val total = recordatorios.sumOf { it.toObject(Recordatorio::class.java).repeticiones }
                            val tomados = recordatorios.count { it.toObject(Recordatorio::class.java).tomado }

                            val cumplimiento = if (total > 0) (tomados * 100) / total else 0

                            val resumen = TextView(this).apply {
                                text = "👤 $nombre - Cumplimiento: $cumplimiento%"
                                textSize = 18f
                                setPadding(0, 12, 0, 12)
                            }
                            layoutCumplimiento.addView(resumen)
                        }
                }
            }
    }
}
