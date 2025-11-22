package com.ucentral.proyectopractica3.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmChannels
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmRestore
import com.ucentral.proyectopractica3.ui.notificaciones.ExactAlarmHelper
import com.ucentral.proyectopractica3.ui.notificaciones.PermissionUtils
import com.ucentral.proyectopractica3.utils.AccountSheet
import de.hdodenhof.circleimageview.CircleImageView

class PacienteHomeActivity : AppCompatActivity() {

    // 👉 Helper para detectar si estamos corriendo pruebas (Espresso)
    private fun isRunningInstrumentationTest(): Boolean =
        try {
            Class.forName("androidx.test.espresso.Espresso")
            true
        } catch (_: ClassNotFoundException) {
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔕 IMPORTANTE:
        // No lanzar diálogos de permisos / ajustes cuando son tests,
        // porque eso manda la Activity al background y rompe Espresso.
        if (!isRunningInstrumentationTest()) {
            // 🔔 1. Nos aseguramos que exista el canal "alarm_channel"
            AlarmChannels.ensureCreated(this)

            // 🔔 2. Pedimos permiso de notificaciones si el teléfono es Android 13+
            PermissionUtils.ensurePostNotificationPermission(this)

            // 🔔 3. Pedimos permiso para alarmas exactas (Android 12/13+)
            ExactAlarmHelper.ensureExactAlarmAllowed(this)

            // 🔔 4. Reprogramamos TODAS las alarmas de este paciente desde Firestore
            AlarmRestore.reprogramAllForCurrentUser(this)
        }

        // Vista normal de tu home
        setContentView(R.layout.activity_paciente_home)

        // Toolbar (ya la tenías)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Botón: ver medicamentos
        val btnVerMedicamentos = findViewById<Button>(R.id.btnVerMedicamentos)
        btnVerMedicamentos.setOnClickListener {
            startActivity(Intent(this, PacienteMedicamentosActivity::class.java))
        }

        // Botón: recordatorios / notificaciones
        val btnRecordatorios = findViewById<Button>(R.id.btnRecordatorios)
        btnRecordatorios.setOnClickListener {
            startActivity(Intent(this, RecordatorioNotificacionesActivity::class.java))
        }

        // Botón: historial
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)
        btnHistorial.setOnClickListener {
            startActivity(Intent(this, PacienteHistorialActivity::class.java))
        }

        // Insets para que no se tape con la status bar / nav bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pacienteRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user, menu)
        return true
    }

    // Cargar avatar en el actionView del menú y abrir el AccountSheet al tocarlo
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu.findItem(R.id.action_avatar)
        val avatarView = item.actionView?.findViewById<CircleImageView>(R.id.imgAvatar)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null && avatarView != null) {
            FirebaseFirestore.getInstance()
                .collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val fotoUrl = doc.getString("fotoUrl")
                    if (!fotoUrl.isNullOrEmpty()) {
                        Glide.with(this).load(fotoUrl).into(avatarView)
                    } else {
                        avatarView.setImageResource(R.drawable.ic_person)
                    }
                }
                .addOnFailureListener {
                    avatarView.setImageResource(R.drawable.ic_person)
                }

            avatarView.setOnClickListener { AccountSheet.show(this) }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    // Al volver de otras pantallas (o incluso si el familiar creó algo mientras tanto),
    // refrescamos avatar y REPROGRAMAMOS todas las alarmas de este paciente.
    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()

        // En tests NO reprogramamos alarmas para evitar accesos a sistema innecesarios
        if (!isRunningInstrumentationTest()) {
            AlarmRestore.reprogramAllForCurrentUser(this)
        }
    }
}
