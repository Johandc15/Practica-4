package com.ucentral.proyectopractica3.auth

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
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

        // ---- OJO ver/ocultar contraseña ----
        var mostrando = false
        binding.btnTogglePass.setOnClickListener {
            mostrando = !mostrando
            binding.etPassword.inputType = if (mostrando)
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
            binding.btnTogglePass.setImageResource(
                if (mostrando) com.ucentral.proyectopractica3.R.drawable.ic_eye_off
                else com.ucentral.proyectopractica3.R.drawable.ic_eye
            )
        }

        // ---- Copiar contraseña ----
        binding.btnCopyPass.setOnClickListener {
            val txt = binding.etPassword.text?.toString().orEmpty()
            if (txt.isEmpty()) {
                toast("No hay contraseña para copiar")
            } else {
                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("password", txt))
                toast("Contraseña copiada")
            }
        }

        // ---- LOGIN ----
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

            // 2) Resolver correo desde email | usuario | cédula
            resolverCorreo(
                id = userInput,
                onOk = { correo -> loginWithEmail(correo, password, key) },
                onErr = { msg ->
                    registerFailedAttempt(key)
                    toast(msg)
                }
            )
        }

        // IR A REGISTRO (igual al tuyo)
        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // OLVIDÓ SU CONTRASEÑA (ahora resuelve el correo si escriben usuario/cédula)
        binding.tvForgotPassword.setOnClickListener {
            val id = binding.etEmail.text.toString().trim()
            if (id.isEmpty()) {
                Toast.makeText(this, "Escribe tu correo, usuario o cédula", Toast.LENGTH_SHORT).show()
            } else {
                resolverCorreo(
                    id = id,
                    onOk = { correo ->
                        auth.sendPasswordResetEmail(correo)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Se envió un correo para restablecer la contraseña", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    },
                    onErr = { msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }
                )
            }
        }
    }

    // ===== FUNCIONES AUXILIARES =====
    private fun loginWithEmail(correo: String, password: String, lockKey: String) {
        auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser

                // Verificar correo antes de pasar
                if (user?.isEmailVerified != true) {
                    user?.sendEmailVerification()
                    Toast.makeText(
                        this,
                        "Tu correo no está verificado. Te reenviamos el correo de verificación.",
                        Toast.LENGTH_LONG
                    ).show()
                    auth.signOut()
                    return@addOnCompleteListener
                }

                // Éxito → resetear intentos y redirigir por rol
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
                registerFailedAttempt(lockKey)
                Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Si 'id' tiene '@' => es correo.
     * Si es numérico => busca por 'cedula'.
     * Si no, busca por 'usuario'.
     * Devuelve el 'correo' para usar con FirebaseAuth.
     */
    private fun resolverCorreo(
        id: String,
        onOk: (String) -> Unit,
        onErr: (String) -> Unit
    ) {
        if (id.contains("@")) { onOk(id); return }
        val campo = if (id.all { it.isDigit() }) "cedula" else "usuario"

        db.collection("usuarios").whereEqualTo(campo, id).limit(1).get()
            .addOnSuccessListener { qs ->
                if (qs.isEmpty) onErr("No se encontró un usuario con ese $campo")
                else {
                    val correo = qs.documents.first().getString("correo")
                    if (correo.isNullOrBlank()) onErr("El usuario no tiene correo registrado")
                    else onOk(correo)
                }
            }
            .addOnFailureListener { e -> onErr("Error consultando usuario: ${e.message}") }
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

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
