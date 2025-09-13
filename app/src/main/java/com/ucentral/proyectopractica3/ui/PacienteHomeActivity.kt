package com.ucentral.proyectopractica3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.ucentral.proyectopractica3.R

class PacienteHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paciente_home)
        val btnVerMedicamentos = findViewById<Button>(R.id.btnVerMedicamentos)
        btnVerMedicamentos.setOnClickListener {
            startActivity(Intent(this, PacienteMedicamentosActivity::class.java))
        }
        val btnRecordatorios = findViewById<Button>(R.id.btnRecordatorios)
        btnRecordatorios.setOnClickListener {
            startActivity(Intent(this, RecordatorioNotificacionesActivity::class.java))
        }
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)
        btnHistorial.setOnClickListener {
            startActivity(Intent(this, PacienteHistorialActivity::class.java))
        }

        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pacienteRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
