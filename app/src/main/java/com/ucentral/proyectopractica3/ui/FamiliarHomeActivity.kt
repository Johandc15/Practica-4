package com.ucentral.proyectopractica3.ui

import android.annotation.SuppressLint
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
import com.ucentral.proyectopractica3.ui.familiar.FamiliarMedicamentosActivity
import com.ucentral.proyectopractica3.ui.familiar.FamiliarPacientesActivity
import com.ucentral.proyectopractica3.ui.familiar.notificaciones.FamiliarNotificacionesActivity
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmChannels
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmRestore
import com.ucentral.proyectopractica3.ui.notificaciones.ExactAlarmHelper
import com.ucentral.proyectopractica3.ui.notificaciones.PermissionUtils
import com.ucentral.proyectopractica3.utils.AccountSheet
import de.hdodenhof.circleimageview.CircleImageView

class FamiliarHomeActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔔 1. Asegurar canal de alarma intrusiva (vibración fuerte, sonido de alarma)
        AlarmChannels.ensureCreated(this)

        // 🔔 2. Pedir permiso de notificaciones (Android 13+ necesita permiso explícito)
        PermissionUtils.ensurePostNotificationPermission(this)

        // 🔔 3. Pedir permiso para alarmas exactas (Android 12/13+ pueden bloquearlas)
        ExactAlarmHelper.ensureExactAlarmAllowed(this)

        // 🔔 4. Reprogramar TODAS las alarmas ligadas a este usuario actual
        //     Esto hace lo mismo que BootReceiver hace tras reiniciar,
        //     pero lo ejecutamos también cada vez que el familiar abre su pantalla principal.
        AlarmRestore.reprogramAllForCurrentUser(this)

        setContentView(R.layout.activity_familiar_home)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val btnListaPacientes = findViewById<Button>(R.id.btnListaPacientes)
        val btnProgramarMedicacion = findViewById<Button>(R.id.btnProgramarMedicacion)
        val btnNotificaciones = findViewById<Button>(R.id.btnNotificaciones)

        btnListaPacientes.setOnClickListener {
            startActivity(Intent(this, FamiliarPacientesActivity::class.java))
        }

        btnProgramarMedicacion.setOnClickListener {
            startActivity(Intent(this, FamiliarMedicamentosActivity::class.java))
        }

        btnNotificaciones.setOnClickListener {
            startActivity(Intent(this, FamiliarNotificacionesActivity::class.java))
        }

        // Ajuste de insets para status bar / nav bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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

    // Cargar avatar en el actionView del menú y abrir AccountSheet al tocarlo
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

    // Refrescar avatar al volver de editar perfil
    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }
}
