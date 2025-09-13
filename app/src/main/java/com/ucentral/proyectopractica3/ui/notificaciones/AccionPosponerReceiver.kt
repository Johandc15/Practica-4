package com.ucentral.proyectopractica3.ui.notificaciones

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class AccionPosponerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra("idRecordatorio") ?: return
        val nombre = intent.getStringExtra("nombreMedicamento") ?: "Medicamento"

        val alarmIntent = Intent(context, AlarmaReceiver::class.java).apply {
            putExtra("idRecordatorio", id)
            putExtra("nombreMedicamento", nombre)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, id.hashCode(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val tiempoEn10Min = System.currentTimeMillis() + 10 * 60 * 1000
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tiempoEn10Min, pendingIntent)
    }
}
