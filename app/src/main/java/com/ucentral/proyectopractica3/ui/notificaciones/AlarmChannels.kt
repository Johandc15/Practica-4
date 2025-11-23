package com.ucentral.proyectopractica3.ui.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build

object AlarmChannels {

    // ID único del canal que TODA la app va a usar para las alarmas
    const val CHANNEL_ID = "alarm_channel"

    fun ensureCreated(ctx: Context) {
        // Solo necesario desde Android 8.0 (Oreo, API 26) en adelante
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Configuración del canal:
            // - IMPORTANCE_HIGH -> prioridad alta / heads-up
            // - vibración fuerte
            // - sonido tipo alarma
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarmas de Medicación",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {

                description = "Recordatorios intrusivos para que el paciente tome el medicamento"

                // Vibración agresiva tipo despertador médico
                enableVibration(true)
                vibrationPattern = longArrayOf(
                    0,   // comienzo inmediato
                    800, // vibra 0.8s
                    400, // pausa 0.4s
                    800, // vibra 0.8s
                    400, // pausa 0.4s
                    1200 // vibra 1.2s
                )

                // Sonido de alarma del sistema (volumen alto por defecto)
                val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                setSound(
                    soundUri,
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )

                // Que se vea completa en lockscreen
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }

            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }
}
