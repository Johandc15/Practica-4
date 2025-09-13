package com.ucentral.proyectopractica3.auth

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.databinding.ActivityRegisterBinding
import com.ucentral.proyectopractica3.model.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val tiposUsuario = listOf("Paciente", "Familiar/Cuidador", "Doctor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposUsuario)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipoUsuario.adapter = adapter

        binding.btnRegister.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val apellido = binding.etApellido.text.toString()
            val usuario = binding.etUsuario.text.toString()
            val correo = binding.etEmail.text.toString()
            val contraseña = binding.etPassword.text.toString()
            val tipoUsuario = binding.spinnerTipoUsuario.selectedItem.toString()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && usuario.isNotEmpty() && correo.isNotEmpty() && contraseña.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val user = User(uid, nombre, apellido, usuario, correo, contraseña, tipoUsuario)

                        // --- CAMBIO: enviar correo de verificación ---
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                // Guardar en Firestore igual que antes
                                db.collection("usuarios").document(uid).set(user).addOnSuccessListener {
                                    Toast.makeText(this, "Usuario registrado. Revisa tu correo para verificar la cuenta.", Toast.LENGTH_LONG).show()

                                    // Cerrar sesión hasta que verifique y volver al login
                                    FirebaseAuth.getInstance().signOut()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
                                }
                            }
                            ?.addOnFailureListener { e ->
                                Toast.makeText(this, "No se pudo enviar el correo de verificación: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        // --- FIN CAMBIO ---

                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
