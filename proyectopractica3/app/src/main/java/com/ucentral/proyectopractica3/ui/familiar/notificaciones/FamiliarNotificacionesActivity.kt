package com.ucentral.proyectopractica3.ui.familiar.notificaciones

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.FamiliarHomeActivity
import com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments.*

class FamiliarNotificacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familiar_notificaciones)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationFamiliarNotificaciones)
        loadFragment(BuscarPacienteNotificacionesFragment()) // Mostrar búsqueda de paciente por defecto

        val btnVolver = findViewById<Button>(R.id.btnVolverFamiliarNotificaciones)
        btnVolver.setOnClickListener {
            startActivity(Intent(this, FamiliarHomeActivity::class.java))
            finish()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_buscar_paciente_notificaciones -> loadFragment(BuscarPacienteNotificacionesFragment())
                R.id.nav_agregar_recordatorio_familiar -> loadFragment(AgregarRecordatorioFamiliarFragment())
                R.id.nav_lista_recordatorios_familiar -> loadFragment(ListaRecordatoriosFamiliarFragment())
                R.id.nav_historial_recordatorios_familiar -> loadFragment(HistorialRecordatoriosFamiliarFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentFamiliarNotificacionesContainer, fragment)
            .commit()
    }
}
