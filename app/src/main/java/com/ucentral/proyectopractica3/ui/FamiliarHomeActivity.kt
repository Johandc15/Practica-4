package com.ucentral.proyectopractica3.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.familiar.FamiliarPacientesActivity
import com.ucentral.proyectopractica3.ui.familiar.FamiliarMedicamentosActivity
import com.ucentral.proyectopractica3.ui.familiar.notificaciones.FamiliarNotificacionesActivity

class FamiliarHomeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_familiar_home)

        val btnListaPacientes = findViewById<Button>(R.id.btnListaPacientes)
        val btnProgramarMedicacion = findViewById<Button>(R.id.btnProgramarMedicacion)
        val btnNotificaciones = findViewById<Button>(R.id.btnNotificaciones)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        btnListaPacientes.setOnClickListener {
            startActivity(Intent(this, FamiliarPacientesActivity::class.java))
        }

        btnProgramarMedicacion.setOnClickListener {
            startActivity(Intent(this, FamiliarMedicamentosActivity::class.java))
        }

        btnNotificaciones.setOnClickListener {
            startActivity(Intent(this, FamiliarNotificacionesActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
