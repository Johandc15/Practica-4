package com.ucentral.proyectopractica3.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.databinding.ActivityLoginBinding
import com.ucentral.proyectopractica3.ui.DoctorHomeActivity
import com.ucentral.proyectopractica3.ui.FamiliarHomeActivity
import com.ucentral.proyectopractica3.ui.PacienteHomeActivity
import kotlin.math.ceil

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // --- Config bloqueo ---
    private val prefs by lazy { getSharedPreferences("login_prefs", MODE_PRIVATE) }
    private val MAX_ATTEMPTS = 3
    private val LOCK_DURATION_MS = 5 * 60 * 1000L // 5 minutos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // LOGIN
        binding.btnLogin.setOnClickListener {
            val userInput = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (userInput.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val key = lockKey(userInput)

            // 1) Verificar si está bloqueado
            val lockUntil = prefs.getLong("lock_until_$key", 0L)
            val now = System.currentTimeMillis()
            if (now < lockUntil) {
                val remainingSec = ceil((lockUntil - now) / 1000.0).toInt()
                val minutes = remainingSec / 60
                val seconds = remainingSec % 60
                Toast.makeText(
                    this,
                    "Demasiados intentos. Intenta de nuevo en ${minutes}m ${seconds}s.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // 2) Intentar login (con correo directo o username -> correo)
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(userInput).matches()) {
                loginWithEmail(userInput, password, key)
            } else {
                db.collection("usuarios")
                    .whereEqualTo("usuario", userInput)
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            val correo = result.documents[0].getString("correo") ?: ""
                            loginWithEmail(correo, password, key)
                        } else {
                            registerFailedAttempt(key) // cuenta como intento fallido
                            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al buscar usuario", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // IR A REGISTRO
        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // OLVIDÓ SU CONTRASEÑA
        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Ingrese su correo para recuperar la contraseña", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Se envió un correo para restablecer la contraseña", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    // ===== FUNCIONES AUXILIARES =====
    private fun loginWithEmail(correo: String, password: String, lockKey: String) {
        auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser

                // 🔒 NUEVO: verificar correo antes de dejar pasar
                if (user?.isEmailVerified != true) {
                    // reenvía verificación (opcional, para ayudar al usuario)
                    user?.sendEmailVerification()
                    Toast.makeText(
                        this,
                        "Tu correo no está verificado. Te enviamos (o re-enviamos) el correo de verificación.",
                        Toast.LENGTH_LONG
                    ).show()

                    // cerrar sesión y NO avanzar
                    auth.signOut()
                    return@addOnCompleteListener
                }

                // Si está verificado → éxito → resetear intentos
                resetAttempts(lockKey)

                val uid = user.uid
                db.collection("usuarios").document(uid).get().addOnSuccessListener { doc ->
                    val tipoUsuario = doc.getString("tipoUsuario")
                    when (tipoUsuario) {
                        "Paciente" -> startActivity(Intent(this, PacienteHomeActivity::class.java))
                        "Doctor" -> startActivity(Intent(this, DoctorHomeActivity::class.java))
                        "Familiar/Cuidador" -> startActivity(Intent(this, FamiliarHomeActivity::class.java))
                        else -> Toast.makeText(this, "Tipo de usuario desconocido", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
            } else {
                // fallo → registrar intento y bloquear si corresponde
                registerFailedAttempt(lockKey)
                Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun registerFailedAttempt(key: String) {
        val current = prefs.getInt("attempts_$key", 0) + 1
        if (current >= MAX_ATTEMPTS) {
            val lockUntil = System.currentTimeMillis() + LOCK_DURATION_MS
            prefs.edit()
                .putInt("attempts_$key", 0)
                .putLong("lock_until_$key", lockUntil)
                .apply()
            Toast.makeText(this, "Demasiados intentos. Bloqueado por 5 minutos.", Toast.LENGTH_LONG).show()
        } else {
            prefs.edit().putInt("attempts_$key", current).apply()
            val remaining = MAX_ATTEMPTS - current
            Toast.makeText(this, "Contraseña incorrecta. Intentos restantes: $remaining", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetAttempts(key: String) {
        prefs.edit()
            .putInt("attempts_$key", 0)
            .putLong("lock_until_$key", 0L)
            .apply()
    }

    private fun lockKey(userInput: String): String = userInput.trim().lowercase()
}

