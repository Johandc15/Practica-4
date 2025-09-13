package com.ucentral.proyectopractica3.ui.notificaciones

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.media.RingtoneManager
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ucentral.proyectopractica3.R

class AlarmaReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val nombre = intent.getStringExtra("nombreMedicamento") ?: "Medicamento"
        val id = intent.getStringExtra("idRecordatorio") ?: ""

        val tomarIntent = Intent(context, AccionTomarReceiver::class.java).apply {
            putExtra("idRecordatorio", id)
        }
        val posponerIntent = Intent(context, AccionPosponerReceiver::class.java).apply {
            putExtra("idRecordatorio", id)
            putExtra("nombreMedicamento", nombre)
        }

        val pendingTomar = PendingIntent.getBroadcast(context, id.hashCode(), tomarIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pendingPosponer = PendingIntent.getBroadcast(context, id.hashCode() + 1, posponerIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "canal_recordatorios")
            .setSmallIcon(R.drawable.ic_pastilla)
            .setContentTitle("💊 Hora del medicamento")
            .setContentText("Toma tu medicamento: $nombre")
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_check, "Tomar", pendingTomar)
            .addAction(R.drawable.ic_snooze, "Posponer", pendingPosponer)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(id.hashCode(), notification)
    }
}
