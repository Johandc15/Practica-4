package com.ucentral.proyectopractica3.ui.doctor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.DoctorHomeActivity

class DoctorListaPacientesActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var layoutPacientes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_lista_pacientes)

        db = FirebaseFirestore.getInstance()
        layoutPacientes = findViewById(R.id.layoutListaPacientesDoctor)

        findViewById<Button>(R.id.btnVolverListaPacientes).setOnClickListener {
            startActivity(Intent(this, DoctorHomeActivity::class.java))
            finish()
        }

        cargarPacientes()
    }

    private fun cargarPacientes() {
        db.collection("usuarios")
            .whereEqualTo("tipoUsuario", "Paciente")
            .get()
            .addOnSuccessListener { result ->
                layoutPacientes.removeAllViews()

                for (doc in result) {
                    val nombre = doc.getString("nombre") ?: "Sin nombre"
                    val correo = doc.getString("correo") ?: "Sin correo"
                    val apellido = doc.getString("apellido") ?: ""
                    val pacienteId = doc.id

                    // Contenedor visual por paciente
                    val tarjeta = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(32, 32, 32, 32)
                        setBackgroundResource(R.drawable.border_gray)
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 0, 0, 32)
                        layoutParams = params
                    }

                    val info = TextView(this).apply {
                        text = "👤 $nombre $apellido\n📧 $correo"
                        textSize = 18f
                    }

                    val btnDiagnostico = Button(this).apply {
                        text = "🧠 Ver Diagnóstico"
                        setBackgroundColor(resources.getColor(R.color.purple_200, null))
                        setTextColor(resources.getColor(android.R.color.white, null))
                        setOnClickListener {
                            val intent = Intent(
                                this@DoctorListaPacientesActivity,
                                DoctorDiagnosticoActivity::class.java
                            )
                            intent.putExtra("pacienteId", pacienteId)
                            startActivity(intent)
                        }
                    }

                    tarjeta.addView(info)
                    tarjeta.addView(btnDiagnostico)
                    layoutPacientes.addView(tarjeta)
                }
            }
    }
}