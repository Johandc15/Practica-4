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
import com.ucentral.proyectopractica3.ui.doctor.DoctorListaPacientesActivity
import com.ucentral.proyectopractica3.ui.doctor.DoctorEstadisticasPacienteActivity
import com.ucentral.proyectopractica3.ui.doctor.DoctorGraficosCumplimientoActivity

class DoctorHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_home)

        val btnListaPacientes = findViewById<Button>(R.id.btnListaPacientes)
        val btnEstadisticas = findViewById<Button>(R.id.btnEstadisticas)
        val btnGraficos = findViewById<Button>(R.id.btnGraficos)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesionDoctor)

        btnListaPacientes.setOnClickListener {
            startActivity(Intent(this, DoctorListaPacientesActivity::class.java))
        }

        btnEstadisticas.setOnClickListener {
            startActivity(Intent(this, DoctorEstadisticasPacienteActivity::class.java))
        }

        btnGraficos.setOnClickListener {
            startActivity(Intent(this, DoctorGraficosCumplimientoActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.doctorRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
