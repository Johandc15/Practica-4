package com.ucentral.proyectopractica3.ui.notificaciones

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("usuarios").document(uid)
            .collection("recordatorios")
            .get()
            .addOnSuccessListener { snap ->
                snap.forEach { doc ->
                    val id = doc.getString("id") ?: doc.id
                    val nombre = doc.getString("nombreMedicamento") ?: "Medicamento"
                    val hora = doc.getString("hora") ?: return@forEach
                    val (h, m) = hora.split(":").map { it.toInt() }
                    AlarmScheduler.scheduleDailyExact(context, id, nombre, h, m)
                }
            }
    }
}
