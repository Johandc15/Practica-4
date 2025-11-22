package com.ucentral.proyectopractica3.ui.notificaciones

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar

object AlarmScheduler {

    private const val TAG = "AlarmScheduler"

    /**
     * Programa una alarma exacta para la PRÓXIMA vez que toque [hora24:minuto].
     * Si esa hora ya pasó hoy, la agenda para mañana.
     *
     * Esta alarma dispara AlarmaReceiver, que:
     *  - muestra notificación intrusiva con pantalla completa
     *  - vibra fuerte + sonido de alarma
     *  - ofrece Tomar / Posponer
     *  - reprograma la alarma para el siguiente día
     */
    fun scheduleDailyExact(
        context: Context,
        recordatorioId: String,
        nombreMedicamento: String,
        hora24: Int,
        minuto: Int
    ) {
        val triggerAtMillis = nextOccurrenceMillis(hora24, minuto)

        // Intent que recibirá AlarmaReceiver cuando llegue la hora
        val intent = Intent(context, AlarmaReceiver::class.java).apply {
            putExtra("recordatorioId", recordatorioId)
            putExtra("nombreMedicamento", nombreMedicamento)
            putExtra("hora", "%02d:%02d".format(hora24, minuto))
            // snooze = false (por defecto)
        }

        // PendingIntent único por recordatorio
        val pi = PendingIntent.getBroadcast(
            context,
            recordatorioId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pi
                )
            } else {
                am.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pi
                )
            }

            Log.d(
                TAG,
                "✅ Alarm programmed for $recordatorioId ($nombreMedicamento) at $hora24:$minuto -> $triggerAtMillis"
            )
        } catch (se: SecurityException) {
            // Esto ocurre si el sistema NO permite alarmas exactas todavía.
            // Para eso existe ExactAlarmHelper.ensureExactAlarmAllowed(...)
            Log.e(TAG, "❌ No se pudo programar alarma exacta (sin permiso): ${se.message}")
        }
    }

    /**
     * Llamado desde AlarmaReceiver DESPUÉS de que la alarma normal sonó.
     * Agenda la misma hora para el día siguiente.
     */
    fun rescheduleNextDaily(
        context: Context,
        recordatorioId: String,
        nombreMedicamento: String,
        hora24: Int,
        minuto: Int
    ) {
        scheduleDailyExact(context, recordatorioId, nombreMedicamento, hora24, minuto)
    }

    /**
     * Llamado desde AccionPosponerReceiver (botón "Posponer 10 min").
     * Agenda una alarma TEMPORAL dentro de [minutes] minutos desde AHORA.
     * Esta alarma llega con "snooze"=true para que AlarmaReceiver NO reprograme el día siguiente todavía.
     */
    fun snoozeMinutes(
        context: Context,
        recordatorioId: String,
        nombreMedicamento: String,
        minutes: Int
    ) {
        val triggerAt = System.currentTimeMillis() + minutes * 60_000L

        val intent = Intent(context, AlarmaReceiver::class.java).apply {
            putExtra("recordatorioId", recordatorioId)
            putExtra("nombreMedicamento", nombreMedicamento)
            putExtra("snooze", true)
        }

        val pi = PendingIntent.getBroadcast(
            context,
            ("SNZ$recordatorioId").hashCode(), // requestCode diferente para snooze
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pi
                )
            } else {
                am.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pi
                )
            }

            Log.d(
                TAG,
                "⏰ Snooze $recordatorioId ($nombreMedicamento) +$minutes min -> $triggerAt"
            )
        } catch (se: SecurityException) {
            Log.e(TAG, "❌ No se pudo programar snooze exacto (sin permiso): ${se.message}")
        }
    }

    /**
     * Cancela la alarma principal para un recordatorio en específico.
     * Útil cuando el usuario borra o edita el recordatorio.
     */
    fun cancel(context: Context, recordatorioId: String) {
        val intent = Intent(context, AlarmaReceiver::class.java)

        val pi = PendingIntent.getBroadcast(
            context,
            recordatorioId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
        pi.cancel()

        Log.d(TAG, "🛑 Alarm canceled for $recordatorioId")
    }

    /**
     * Devuelve el próximo timestamp (millis) para cierta hora del día.
     * Si esa hora ya pasó hoy, devuelve mañana a esa hora.
     */
    private fun nextOccurrenceMillis(hora24: Int, minuto: Int): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.HOUR_OF_DAY, hora24)
            set(Calendar.MINUTE, minuto)
        }

        // si la hora ya pasó hoy, saltamos al día siguiente
        if (cal.timeInMillis <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        return cal.timeInMillis
    }
}
