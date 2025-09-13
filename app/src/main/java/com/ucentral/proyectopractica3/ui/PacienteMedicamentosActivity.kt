package com.ucentral.proyectopractica3.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.medicamentos.AgregarMedicamentoFragment
import com.ucentral.proyectopractica3.ui.medicamentos.ConfiguracionMedicamentoFragment
import com.ucentral.proyectopractica3.ui.medicamentos.ListaMedicamentosFragment

class PacienteMedicamentosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paciente_medicamentos)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        loadFragment(ListaMedicamentosFragment()) // Carga la lista por defecto
        val btnVolver = findViewById<Button>(R.id.btnVolverPaciente)
        btnVolver.setOnClickListener {
            finish() // vuelve a la activity anterior (PacienteHomeActivity)
        }


        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_agregar -> loadFragment(AgregarMedicamentoFragment())
                R.id.nav_lista -> loadFragment(ListaMedicamentosFragment())
                R.id.nav_config -> loadFragment(ConfiguracionMedicamentoFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
