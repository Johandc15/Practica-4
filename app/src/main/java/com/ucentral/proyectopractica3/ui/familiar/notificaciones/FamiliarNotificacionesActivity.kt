package com.ucentral.proyectopractica3.ui.familiar.notificaciones

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.FamiliarHomeActivity
import com.ucentral.proyectopractica3.ui.familiar.notificaciones.fragments.*
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado

class FamiliarNotificacionesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familiar_notificaciones)

        val bottomNav =
            findViewById<BottomNavigationView>(R.id.bottomNavigationFamiliarNotificaciones)
        val btnVolver = findViewById<Button>(R.id.btnVolverFamiliarNotificaciones)

        // Mostrar búsqueda de paciente por defecto
        loadFragment(BuscarPacienteNotificacionesFragment())
        bottomNav.selectedItemId = R.id.nav_buscar_paciente_notificaciones

        btnVolver.setOnClickListener {
            startActivity(Intent(this, FamiliarHomeActivity::class.java))
            finish()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_buscar_paciente_notificaciones -> {
                    loadFragment(BuscarPacienteNotificacionesFragment())
                    true
                }

                R.id.nav_agregar_recordatorio_familiar -> {
                    if (hayPacienteSeleccionado()) {
                        loadFragment(AgregarRecordatorioFamiliarFragment())
                    } else {
                        bottomNav.selectedItemId = R.id.nav_buscar_paciente_notificaciones
                    }
                    true
                }

                R.id.nav_lista_recordatorios_familiar -> {
                    if (hayPacienteSeleccionado()) {
                        loadFragment(ListaRecordatoriosFamiliarFragment())
                    } else {
                        bottomNav.selectedItemId = R.id.nav_buscar_paciente_notificaciones
                    }
                    true
                }

                R.id.nav_historial_recordatorios_familiar -> {
                    if (hayPacienteSeleccionado()) {
                        loadFragment(HistorialRecordatoriosFamiliarFragment())
                    } else {
                        bottomNav.selectedItemId = R.id.nav_buscar_paciente_notificaciones
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun hayPacienteSeleccionado(): Boolean {
        val id = PacienteSeleccionado.pacienteId
        return if (id.isNullOrBlank()) {
            Toast.makeText(
                this,
                "Selecciona primero un paciente de la lista.",
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentFamiliarNotificacionesContainer, fragment)
            .commit()
    }
}
