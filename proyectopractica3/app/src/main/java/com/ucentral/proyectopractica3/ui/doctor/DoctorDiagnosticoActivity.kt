package com.ucentral.proyectopractica3.ui.doctor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import java.util.*

class DoctorDiagnosticoActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var etSintomas: EditText
    private lateinit var etAntecedentes: EditText
    private lateinit var etHabitos: EditText
    private lateinit var tvResultado: TextView
    private lateinit var btnGenerar: Button
    private lateinit var btnGuardar: Button

    private var resultado: String = ""
    private var pacienteId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_diagnostico)

        db = FirebaseFirestore.getInstance()
        pacienteId = intent.getStringExtra("pacienteId") ?: ""

        etSintomas = findViewById(R.id.etSintomas)
        etAntecedentes = findViewById(R.id.etAntecedentes)
        etHabitos = findViewById(R.id.etHabitos)
        tvResultado = findViewById(R.id.tvResultadoDiagnostico)
        btnGenerar = findViewById(R.id.btnGenerarDiagnostico)
        btnGuardar = findViewById(R.id.btnGuardarDiagnostico)

        val btnVolver = findViewById<Button>(R.id.btnVolverDiagnostico)
        btnVolver.setOnClickListener {
            finish() // Cierra esta activity y vuelve a la anterior (Lista de Pacientes)
        }


        btnGenerar.setOnClickListener {
            resultado = generarDiagnostico(etSintomas.text.toString(), etHabitos.text.toString(), etAntecedentes.text.toString())
            tvResultado.text = "🧠 Diagnóstico:\n\n$resultado"
        }

        btnGuardar.setOnClickListener {
            if (resultado.isNotEmpty()) {
                val data = mapOf(
                    "fecha" to System.currentTimeMillis(),
                    "resultado" to resultado,
                    "sintomas" to etSintomas.text.toString(),
                    "habitos" to etHabitos.text.toString(),
                    "antecedentes" to etAntecedentes.text.toString()
                )
                db.collection("usuarios").document(pacienteId)
                    .collection("diagnosticos").document(UUID.randomUUID().toString())
                    .set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun generarDiagnostico(sintomas: String, habitos: String, antecedentes: String): String {
        return when {
            "fiebre" in sintomas.lowercase() -> "Posible infección o gripe."
            "presión" in antecedentes.lowercase() -> "Controlar hipertensión y dieta."
            "sedentarismo" in habitos.lowercase() -> "Se recomienda actividad física moderada."
            else -> "Requiere evaluación médica detallada."
        }
    }
}
