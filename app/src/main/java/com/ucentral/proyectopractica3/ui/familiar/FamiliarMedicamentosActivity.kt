// src/main/java/com/ucentral/proyectopractica3/ui/familiar/FamiliarMedicamentosActivity.kt
package com.ucentral.proyectopractica3.ui.familiar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.FamiliarHomeActivity
import com.ucentral.proyectopractica3.ui.familiar.fragments.AgregarMedicamentoFamiliarFragment
import com.ucentral.proyectopractica3.ui.familiar.fragments.ListaMedicamentosFamiliarFragment
import com.ucentral.proyectopractica3.ui.familiar.fragments.ConfiguracionMedicamentosFamiliarFragment
import com.ucentral.proyectopractica3.ui.familiar.fragments.BuscarPacienteFragment
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado

class FamiliarMedicamentosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familiar_medicamentos)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationFamiliarMedicamentos)
        val btnVolver = findViewById<Button>(R.id.btnVolverFamiliarMedicamentos)

        // Mostrar BuscarPaciente por defecto
        loadFragment(BuscarPacienteFragment())
        bottomNav.selectedItemId = R.id.nav_buscar_paciente

        btnVolver.setOnClickListener {
            startActivity(Intent(this, FamiliarHomeActivity::class.java))
            finish()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_buscar_paciente -> {
                    loadFragment(BuscarPacienteFragment())
                    true
                }

                R.id.nav_agregar_medicamento -> {
                    if (hayPacienteSeleccionado()) {
                        loadFragment(AgregarMedicamentoFamiliarFragment())
                    } else {
                        // No cambiamos de pestaña, volvemos visualmente a "Buscar paciente"
                        bottomNav.selectedItemId = R.id.nav_buscar_paciente
                    }
                    true
                }

                R.id.nav_lista_medicamentos -> {
                    if (hayPacienteSeleccionado()) {
                        loadFragment(ListaMedicamentosFamiliarFragment())
                    } else {
                        bottomNav.selectedItemId = R.id.nav_buscar_paciente
                    }
                    true
                }

                R.id.nav_configuracion_medicamentos -> {
                    if (hayPacienteSeleccionado()) {
                        loadFragment(ConfiguracionMedicamentosFamiliarFragment())
                    } else {
                        bottomNav.selectedItemId = R.id.nav_buscar_paciente
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
            .replace(R.id.fragmentContainerFamiliarMedicamentos, fragment)
            .commit()
    }
}
