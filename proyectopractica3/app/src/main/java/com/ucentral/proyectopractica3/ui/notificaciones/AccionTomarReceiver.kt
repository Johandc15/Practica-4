package com.ucentral.proyectopractica3.ui.notificaciones

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccionTomarReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("recordatorioId") ?: return
        val nombre = intent.getStringExtra("nombreMedicamento") ?: "Medicamento"

        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            FirebaseFirestore.getInstance()
                .collection("usuarios").document(uid)
                .collection("recordatorios").document(id)
                .update("tomado", true)
        }

        NotificationManagerCompat.from(context).cancel(id.hashCode())
        Toast.makeText(context, "Tomado: $nombre", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun intent(ctx: Context, from: Intent): Intent =
            Intent(ctx, AccionTomarReceiver::class.java).apply {
                putExtras(from.extras ?: return@apply)
            }
    }
}
