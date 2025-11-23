package com.ucentral.proyectopractica3.ui.notificaciones

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.ucentral.proyectopractica3.R

class AlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AlarmChannels.ensureCreated(context)

        val id = intent.getStringExtra("recordatorioId") ?: ""
        val nombre = intent.getStringExtra("nombreMedicamento") ?: "Medicamento"
        val hora = intent.getStringExtra("hora") ?: ""

        val tomarPI = PendingIntent.getBroadcast(
            context, ("TAK$id").hashCode(),
            Intent(context, AccionTomarReceiver::class.java).putExtras(intent),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val posponerPI = PendingIntent.getBroadcast(
            context, ("SNO$id").hashCode(),
            Intent(context, AccionPosponerReceiver::class.java).putExtras(intent).putExtra("minutos", 10),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val fullPI = PendingIntent.getActivity(
            context, ("FUL$id").hashCode(),
            Intent(context, AlarmRingActivity::class.java).putExtras(intent),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(context, AlarmChannels.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("¡Hora del medicamento!")
            .setContentText("💊 $nombre ${if (hora.isNotBlank()) "• $hora" else ""}")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullPI, true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setVibrate(longArrayOf(0,800,400,800,400,1200))
            .addAction(R.drawable.ic_done, "Tomar", tomarPI)
            .addAction(R.drawable.ic_snooze, "Posponer 10 min", posponerPI)
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(id.hashCode(), notif)

        val wasSnooze = intent.getBooleanExtra("snooze", false)
        if (!wasSnooze && hora.isNotBlank()) {
            val (h, m) = hora.split(":").map { it.toInt() }
            AlarmScheduler.rescheduleNextDaily(context, id, nombre, h, m)
        }
    }
}
