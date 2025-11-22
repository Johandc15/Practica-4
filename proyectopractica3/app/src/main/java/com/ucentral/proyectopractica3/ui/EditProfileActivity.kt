package com.ucentral.proyectopractica3.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    private var pickedImageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            pickedImageUri = uri
            binding.ivAvatar.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Editar perfil"

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Sin sesión activa", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        // Cargar datos actuales de Firestore
        db.collection("usuarios").document(user.uid).get()
            .addOnSuccessListener { doc ->
                val nombre = doc.getString("nombre") ?: ""
                val apellido = doc.getString("apellido") ?: ""
                val usuario = doc.getString("usuario") ?: ""
                val fotoUrl = doc.getString("fotoUrl")

                binding.etNombre.setText(nombre)
                binding.etApellido.setText(apellido)
                binding.etUsuario.setText(usuario)

                // Cargar avatar actual si existe
                if (!fotoUrl.isNullOrEmpty()) {
                    Glide.with(this).load(fotoUrl).placeholder(R.drawable.ic_person).into(binding.ivAvatar)
                } else {
                    binding.ivAvatar.setImageResource(R.drawable.ic_person)
                }
            }

        // Abrir selector de imagen
        binding.btnCambiarFoto.setOnClickListener { pickImage.launch("image/*") }

        // Guardar cambios
        binding.btnGuardar.setOnClickListener {
            saveProfile()
        }

        // Si venimos desde "Cambiar foto" del AccountSheet, abre el selector de una
        if (intent.getBooleanExtra("openPhotoPicker", false)) {
            binding.btnCambiarFoto.post { binding.btnCambiarFoto.performClick() }
        }
    }

    private fun saveProfile() {
        val user = auth.currentUser ?: return
        val uid = user.uid

        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val usuario = binding.etUsuario.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty()) {
            Toast.makeText(this, "Completa nombre, apellido y usuario", Toast.LENGTH_SHORT).show()
            return
        }

        // 1) Subir foto si el usuario eligió una
        val uploadTask = if (pickedImageUri != null) {
            val ref = storage.reference.child("avatars/$uid.jpg")
            ref.putFile(pickedImageUri!!)
                .continueWithTask { ref.downloadUrl } // obtener URL pública
        } else null

        val runUpdate: (String?) -> Unit = { photoUrl ->
            // 2) Actualizar Firestore
            val updates = hashMapOf<String, Any>(
                "nombre" to nombre,
                "apellido" to apellido,
                "usuario" to usuario
            )
            if (photoUrl != null) updates["fotoUrl"] = photoUrl

            db.collection("usuarios").document(uid).update(updates)
                .addOnSuccessListener {
                    // 3) Actualizar perfil visible en FirebaseAuth (displayName y photoUrl)
                    val req = UserProfileChangeRequest.Builder()
                        .setDisplayName("$nombre $apellido")
                        .apply { if (photoUrl != null) setPhotoUri(Uri.parse(photoUrl)) }
                        .build()
                    auth.currentUser?.updateProfile(req)?.addOnCompleteListener {
                        Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        if (uploadTask != null) {
            uploadTask
                .addOnSuccessListener { uri -> runUpdate(uri.toString()) }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "No se pudo subir la foto: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            runUpdate(null)
        }
    }
}
