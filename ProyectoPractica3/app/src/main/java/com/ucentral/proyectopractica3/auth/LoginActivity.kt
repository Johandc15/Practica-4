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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnLogin.setOnClickListener {
            val userInput = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (userInput.isNotEmpty() && password.isNotEmpty()) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(userInput).matches()) {
                    loginWithEmail(userInput, password)
                } else {
                    db.collection("usuarios")
                        .whereEqualTo("usuario", userInput)
                        .get()
                        .addOnSuccessListener { result ->
                            if (!result.isEmpty) {
                                val correo = result.documents[0].getString("correo") ?: ""
                                loginWithEmail(correo, password)
                            } else {
                                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al buscar usuario", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginWithEmail(correo: String, password: String) {
        auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""
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
                Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
