package com.ucentral.proyectopractica3.ui.notificaciones

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class AccionPosponerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("recordatorioId") ?: return
        val nombre = intent.getStringExtra("nombreMedicamento") ?: "Medicamento"
        val minutos = intent.getIntExtra("minutos", 10)

        NotificationManagerCompat.from(context).cancel(id.hashCode())
        AlarmScheduler.snoozeMinutes(context, id, nombre, minutos)
    }

    companion object {
        fun intent(ctx: Context, from: Intent, minutes: Int): Intent =
            Intent(ctx, AccionPosponerReceiver::class.java).apply {
                putExtras(from.extras ?: return@apply)
                putExtra("minutos", minutes)
            }
    }
}
