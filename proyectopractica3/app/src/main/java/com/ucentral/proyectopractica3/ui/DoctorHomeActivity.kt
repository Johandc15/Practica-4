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
import com.ucentral.proyectopractica3.ui.doctor.DoctorEstadisticasPacienteActivity
import com.ucentral.proyectopractica3.ui.doctor.DoctorGraficosCumplimientoActivity
import com.ucentral.proyectopractica3.ui.doctor.DoctorListaPacientesActivity
import com.ucentral.proyectopractica3.utils.AccountSheet
import de.hdodenhof.circleimageview.CircleImageView

class DoctorHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_home)

        // Toolbar (nuevo)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val btnListaPacientes = findViewById<Button>(R.id.btnListaPacientes)
        val btnEstadisticas = findViewById<Button>(R.id.btnEstadisticas)
        val btnGraficos = findViewById<Button>(R.id.btnGraficos)

        btnListaPacientes.setOnClickListener {
            startActivity(Intent(this, DoctorListaPacientesActivity::class.java))
        }
        btnEstadisticas.setOnClickListener {
            startActivity(Intent(this, DoctorEstadisticasPacienteActivity::class.java))
        }
        btnGraficos.setOnClickListener {
            startActivity(Intent(this, DoctorGraficosCumplimientoActivity::class.java))
        }

        // (Eliminado: btnCerrarSesionDoctor – ahora se usa el menú del Toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.doctorRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user, menu)
        return true
    }

    // Cargar avatar y abrir AccountSheet al tocarlo
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

    // Refresca el avatar al volver de EditProfile
    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }
}

