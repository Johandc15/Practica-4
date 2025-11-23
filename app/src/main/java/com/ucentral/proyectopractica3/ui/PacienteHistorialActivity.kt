package com.ucentral.proyectopractica3.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.historial.HistorialFragment

class PacienteHistorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente_historial)

        supportFragmentManager.beginTransaction()
            .replace(R.id.historialContainer, HistorialFragment())
            .commit()

        val btnVolver = findViewById<Button>(R.id.btnVolverHistorial)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
