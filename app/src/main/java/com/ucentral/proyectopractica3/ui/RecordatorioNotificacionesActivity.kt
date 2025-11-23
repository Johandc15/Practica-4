package com.ucentral.proyectopractica3.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.recordatorios.AgregarRecordatorioFragment
import com.ucentral.proyectopractica3.ui.recordatorios.NotificacionesFragment

class RecordatorioNotificacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordatorio_notificaciones)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationRecordatorio)
        loadFragment(AgregarRecordatorioFragment()) // Por defecto

        val btnVolver = findViewById<Button>(R.id.btnVolverRecordatorio)
        btnVolver.setOnClickListener {
            finish() // Regresar a la actividad anterior (PacienteHomeActivity)
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_recordatorio -> loadFragment(AgregarRecordatorioFragment())
                R.id.nav_notificaciones -> loadFragment(NotificacionesFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentRecordatorioContainer, fragment)
            .commit()
    }
}
