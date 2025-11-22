package com.ucentral.proyectopractica3.utils

import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.EditProfileActivity
import com.ucentral.proyectopractica3.ui.MainActivity

/**
 * Hoja inferior (BottomSheet) reutilizable para el menú de cuenta.
 * Opciones: Ver información, Editar perfil, Cambiar foto, Cerrar sesión.
 *
 * Uso: AccountSheet.show(activity)
 */
object AccountSheet {

    fun show(activity: AppCompatActivity) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        if (user == null) {
            Toast.makeText(activity, "Sin sesión activa", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = BottomSheetDialog(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.bottomsheet_account, null)
        dialog.setContentView(view)

        val btnVerInfo = view.findViewById<TextView>(R.id.btnViewAccount)
        val btnEditarPerfil = view.findViewById<TextView>(R.id.btnEditProfile)
        val btnCambiarFoto = view.findViewById<TextView>(R.id.btnChangePhoto)
        val btnCerrarSesion = view.findViewById<TextView>(R.id.btnSignOut)

        // Ver información del usuario (lee Firestore/usuarios/{uid})
        btnVerInfo.setOnClickListener {
            db.collection("usuarios").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    val nombre = doc.getString("nombre") ?: ""
                    val apellido = doc.getString("apellido") ?: ""
                    val usuario = doc.getString("usuario") ?: ""
                    val correo = doc.getString("correo") ?: (user.email ?: "")
                    val tipo = doc.getString("tipoUsuario") ?: ""
                    val info = """
                        Nombre:  $nombre $apellido
                        Usuario: $usuario
                        Correo:  $correo
                        Tipo:    $tipo
                    """.trimIndent()

                    AlertDialog.Builder(activity)
                        .setTitle("Mi cuenta")
                        .setMessage(info)
                        .setPositiveButton("Cerrar", null)
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(activity)
                        .setTitle("Mi cuenta")
                        .setMessage("Correo: ${user.email ?: "desconocido"}")
                        .setPositiveButton("Cerrar", null)
                        .show()
                }
        }

        // Ir a editar perfil
        btnEditarPerfil.setOnClickListener {
            dialog.dismiss()
            activity.startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        // Cambiar foto (abre EditProfile con el selector de imagen)
        btnCambiarFoto.setOnClickListener {
            dialog.dismiss()
            val i = Intent(activity, EditProfileActivity::class.java)
            i.putExtra("openPhotoPicker", true)
            activity.startActivity(i)
        }

        // Cerrar sesión
        btnCerrarSesion.setOnClickListener {
            dialog.dismiss()
            auth.signOut()
            val i = Intent(activity, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(i)
        }

        dialog.show()
    }
}