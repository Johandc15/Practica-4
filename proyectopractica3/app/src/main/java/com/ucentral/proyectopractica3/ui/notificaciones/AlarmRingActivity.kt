package com.ucentral.proyectopractica3.ui.notificaciones

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ucentral.proyectopractica3.R

class AlarmRingActivity : AppCompatActivity() {
    private var mp: MediaPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_ring)

        val nombre = intent.getStringExtra("nombreMedicamento") ?: "Medicamento"
        findViewById<TextView>(R.id.tvTituloAlarm).text = "Hora de tomar: $nombre"

        @Suppress("DEPRECATION")
        run {
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            wakeLock = pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "meditime:AlarmWakeLock"
            ).apply { acquire(5 * 60 * 1000L) }
        }

        val tone = Uri.parse("android.resource://${packageName}/${R.raw.alarm_tone}")
        mp = MediaPlayer.create(this, tone).apply { isLooping = true; start() }

        findViewById<Button>(R.id.btnTomarAhora).setOnClickListener {
            sendBroadcast(AccionTomarReceiver.intent(this, intent)); finish()
        }
        findViewById<Button>(R.id.btnPosponer).setOnClickListener {
            sendBroadcast(AccionPosponerReceiver.intent(this, intent, 10)); finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop(); mp?.release(); mp = null
        wakeLock?.let { if (it.isHeld) it.release() }
    }
}
