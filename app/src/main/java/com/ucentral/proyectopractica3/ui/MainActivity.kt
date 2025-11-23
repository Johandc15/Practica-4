package com.ucentral.proyectopractica3.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ucentral.proyectopractica3.auth.LoginActivity
import com.ucentral.proyectopractica3.auth.RegisterActivity
import com.ucentral.proyectopractica3.databinding.ActivityMainBinding
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmChannels

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔔 Canal único de alarmas (sonido de alarma, vibración fuerte)
        // Esto reemplaza tu código viejo que creaba "canal_recordatorios"
        AlarmChannels.ensureCreated(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajustar padding para status bar / nav bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Botón "Iniciar sesión"
        binding.btnIniciarSesion.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Botón "Registrarse"
        binding.btnRegistrarse.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
