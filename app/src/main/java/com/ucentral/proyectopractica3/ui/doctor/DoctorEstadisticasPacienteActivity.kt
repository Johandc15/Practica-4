package com.ucentral.proyectopractica3.ui.doctor

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.ui.DoctorHomeActivity

class DoctorEstadisticasPacienteActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etCorreoBuscar: EditText
    private lateinit var btnBuscar: Button
    private lateinit var layoutResultados: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_estadisticas_paciente)

        db = FirebaseFirestore.getInstance()
        etCorreoBuscar = findViewById(R.id.etCorreoPacienteEstadistica)
        btnBuscar = findViewById(R.id.btnBuscarEstadistica)
        layoutResultados = findViewById(R.id.layoutEstadisticasPaciente)

        findViewById<Button>(R.id.btnVolverEstadisticasPaciente).setOnClickListener {
            startActivity(Intent(this, DoctorHomeActivity::class.java))
            finish()
        }

        btnBuscar.setOnClickListener {
            val correo = etCorreoBuscar.text.toString().trim()
            buscarYMostrarEstadisticas(correo)
        }
    }

    private fun buscarYMostrarEstadisticas(correo: String) {
        db.collection("usuarios")
            .whereEqualTo("correo", correo)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(this, "Paciente no encontrado", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                val paciente = result.documents.first()
                val pacienteId = paciente.id

                db.collection("usuarios").document(pacienteId)
                    .collection("recordatorios")
                    .get()
                    .addOnSuccessListener { records ->
                        var total = 0
                        var tomados = 0
                        layoutResultados.removeAllViews()

                        for (doc in records) {
                            val rec = doc.toObject(Recordatorio::class.java)
                            total += rec.repeticiones
                            if (rec.tomado) tomados += 1

                            val tarjeta = LinearLayout(this).apply {
                                orientation = LinearLayout.VERTICAL
                                setPadding(24, 16, 24, 16)
                                setBackgroundResource(R.drawable.border_gray) // asegúrate de tener este fondo
                                val params = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                params.setMargins(0, 0, 0, 24)
                                layoutParams = params
                            }

                            val titulo = TextView(this).apply {
                                text = "💊 ${rec.nombreMedicamento}"
                                textSize = 18f
                                setTypeface(null, android.graphics.Typeface.BOLD)
                            }

                            val detalles = TextView(this).apply {
                                text = "🕒 Hora: ${rec.hora}\n📌 Estado: ${if (rec.tomado) "✅ Tomado" else "⏳ Pendiente"}"
                                textSize = 16f
                            }

                            tarjeta.addView(titulo)
                            tarjeta.addView(detalles)
                            layoutResultados.addView(tarjeta)

                        }

                        val resumen = LinearLayout(this).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(24, 16, 24, 16)
                            setBackgroundResource(R.drawable.border_green) // crea un borde verde si deseas
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(0, 32, 0, 32)
                            layoutParams = params
                        }

                        val resumenTexto = TextView(this).apply {
                            text = "📊 Cumplimiento del tratamiento:\n${if (total > 0) "${(tomados * 100) / total}%" else "0%"}"
                            textSize = 20f
                            setTypeface(null, android.graphics.Typeface.BOLD)
                            setTextColor(resources.getColor(R.color.teal_700, null))
                        }

                        resumen.addView(resumenTexto)
                        layoutResultados.addView(resumen)

                    }
            }
    }
}
