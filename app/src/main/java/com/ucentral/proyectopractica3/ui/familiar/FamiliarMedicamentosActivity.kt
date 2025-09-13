// src/main/java/com/ucentral/proyectopractica3/ui/familiar/FamiliarMedicamentosActivity.kt
package com.ucentral.proyectopractica3.ui.familiar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.FamiliarHomeActivity
import com.ucentral.proyectopractica3.ui.familiar.fragments.AgregarMedicamentoFamiliarFragment
import com.ucentral.proyectopractica3.ui.familiar.fragments.ListaMedicamentosFamiliarFragment
import com.ucentral.proyectopractica3.ui.familiar.fragments.ConfiguracionMedicamentosFamiliarFragment
import com.ucentral.proyectopractica3.ui.familiar.fragments.BuscarPacienteFragment

class FamiliarMedicamentosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_familiar_medicamentos)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationFamiliarMedicamentos)
        loadFragment(BuscarPacienteFragment()) // Mostrar BuscarPaciente por defecto

        val btnVolver = findViewById<Button>(R.id.btnVolverFamiliarMedicamentos)
        btnVolver.setOnClickListener {
            startActivity(Intent(this, FamiliarHomeActivity::class.java))
            finish()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_buscar_paciente -> loadFragment(BuscarPacienteFragment())
                R.id.nav_agregar_medicamento -> loadFragment(AgregarMedicamentoFamiliarFragment())
                R.id.nav_lista_medicamentos -> loadFragment(ListaMedicamentosFamiliarFragment())
                R.id.nav_configuracion_medicamentos -> loadFragment(ConfiguracionMedicamentosFamiliarFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerFamiliarMedicamentos, fragment)
            .commit()
    }
}
