package com.ucentral.proyectopractica3.ui.notificaciones

import android.content.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccionTomarReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("idRecordatorio") ?: return
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("usuarios").document(uid)
            .collection("recordatorios").document(id)
            .update("tomado", true)
    }
}
