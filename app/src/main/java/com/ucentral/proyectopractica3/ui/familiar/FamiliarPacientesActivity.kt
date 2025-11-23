package com.ucentral.proyectopractica3.ui.familiar

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.familiar.fragments.AgregarPacienteFragment
import com.ucentral.proyectopractica3.ui.familiar.fragments.ListaPacientesAsociadosFragment

class FamiliarPacientesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familiar_pacientes)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationPacientes)
        val btnVolver = findViewById<Button>(R.id.btnVolverFamiliar)

        loadFragment(ListaPacientesAsociadosFragment()) // Muestra lista por defecto

        btnVolver.setOnClickListener {
            finish() // Volver al FamiliarHomeActivity
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_agregar_paciente -> loadFragment(AgregarPacienteFragment())
                R.id.nav_lista_pacientes -> loadFragment(ListaPacientesAsociadosFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentPacientesContainer, fragment)
            .commit()
    }
}
