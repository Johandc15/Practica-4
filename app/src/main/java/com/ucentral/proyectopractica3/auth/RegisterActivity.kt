// com/ucentral/proyectopractica3/auth/RegisterActivity.kt
package com.ucentral.proyectopractica3.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.TextPaint
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.databinding.ActivityRegisterBinding
import com.ucentral.proyectopractica3.model.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var mostrandoPass = false
    private var mostrandoConfirm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // ======= Spinners =======
        val tiposUsuario = listOf("Paciente", "Familiar/Cuidador", "Doctor")
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposUsuario)
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipoUsuario.adapter = tipoAdapter

        val rhAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.rh_tipos,
            android.R.layout.simple_spinner_item
        )
        rhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRh.adapter = rhAdapter

        // ======= Ir a Login =======
        binding.tvIrLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // ======= Términos y Condiciones =======
        binding.btnRegister.isEnabled = false
        binding.btnRegister.alpha = 0.5f

        binding.cbAceptarTyC.setOnCheckedChangeListener { _, isChecked ->
            binding.btnRegister.isEnabled = isChecked
            binding.btnRegister.alpha = if (isChecked) 1f else 0.5f
        }

        val full = "He leído y acepto los Términos y Condiciones y la Política de Privacidad"
        val spannable = SpannableString(full)

        fun spanClickable(texto: String, onClick: () -> Unit) {
            val start = full.indexOf(texto)
            if (start >= 0) {
                val end = start + texto.length
                spannable.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) { onClick() }
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds); ds.isUnderlineText = true
                    }
                }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannable.setSpan(
                    ForegroundColorSpan(resources.getColor(R.color.blue_purple, theme)),
                    start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        spanClickable("Términos y Condiciones") { mostrarTerminos() }
        spanClickable("Política de Privacidad") { mostrarPrivacidad() }

        binding.tvTextoTyC.text = spannable
        binding.tvTextoTyC.movementMethod = LinkMovementMethod.getInstance()

        // ======= Ojo ver/ocultar contraseña (FIX a EditText) =======
        binding.btnTogglePass.setOnClickListener {
            mostrandoPass = !mostrandoPass
            togglePasswordVisibility(binding.etPassword, mostrandoPass)
            binding.btnTogglePass.setImageResource(
                if (mostrandoPass) R.drawable.ic_eye_off else R.drawable.ic_eye
            )
        }
        binding.btnToggleConfirm.setOnClickListener {
            mostrandoConfirm = !mostrandoConfirm
            togglePasswordVisibility(binding.etConfirmPassword, mostrandoConfirm)
            binding.btnToggleConfirm.setImageResource(
                if (mostrandoConfirm) R.drawable.ic_eye_off else R.drawable.ic_eye
            )
        }

        // ======= Checklist dinámico contraseña (✅/❌) =======
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pwd = s?.toString().orEmpty()
                setHelperForPassword(pwd, binding.etConfirmPassword.text.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirm = s?.toString().orEmpty()
                setHelperForPassword(binding.etPassword.text.toString(), confirm)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // ======= Registrar =======
        binding.btnRegister.setOnClickListener {
            if (!binding.cbAceptarTyC.isChecked) {
                toast("Debes aceptar los Términos y Condiciones para continuar"); return@setOnClickListener
            }

            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()
            val usuario = binding.etUsuario.text.toString().trim()
            val correo = binding.etEmail.text.toString().trim()
            val cedula = binding.etCedula.text.toString().trim()
            val eps = binding.etEps.text.toString().trim()
            val rh = binding.spinnerRh.selectedItem?.toString()?.trim().orEmpty()
            val tipoUsuario = binding.spinnerTipoUsuario.selectedItem?.toString().orEmpty()
            val password = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            // Validaciones mínimas
            if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() ||
                correo.isEmpty() || cedula.isEmpty() || eps.isEmpty() || rh.isEmpty() ||
                password.isEmpty() || confirm.isEmpty()
            ) {
                toast("Completa todos los campos"); return@setOnClickListener
            }
            if (!cedula.all { it.isDigit() }) {
                toast("La cédula debe contener solo números"); return@setOnClickListener
            }
            if (!isStrongPassword(password)) {
                binding.tvPasswordHelper.text = passwordRequirementsMessage(password)
                toast("La contraseña no cumple los requisitos"); return@setOnClickListener
            }
            if (password != confirm) {
                binding.etConfirmPassword.error = "Las contraseñas no coinciden"
                binding.tvPasswordHelper.text = "Las contraseñas no coinciden"
                return@setOnClickListener
            } else {
                binding.etConfirmPassword.error = null
            }

            // Crear usuario + enviar verificación
            auth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = User(
                        uid = uid,
                        nombre = nombre,
                        apellido = apellido,
                        usuario = usuario,
                        correo = correo,
                        contraseña = password, // En producción, evita guardar texto plano
                        tipoUsuario = tipoUsuario,
                        cedula = cedula,
                        eps = eps,
                        rh = rh
                    )

                    auth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            db.collection("usuarios").document(uid).set(user)
                                .addOnSuccessListener {
                                    val metadata = mapOf(
                                        "aceptoTyC" to true,
                                        "aceptoTyC_fecha" to Timestamp.now()
                                    )
                                    db.collection("usuarios").document(uid).update(metadata)

                                    toast("Usuario registrado. Revisa tu correo para verificar la cuenta.")
                                    FirebaseAuth.getInstance().signOut()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    toast("Error al guardar en la base de datos")
                                }
                        }
                        ?.addOnFailureListener { e ->
                            toast("No se pudo enviar el correo de verificación: ${e.message}")
                        }
                } else {
                    toast("Error: ${task.exception?.message}")
                }
            }
        }
    }

    // ======= Ayudas UI / Checklist =======
    private fun setHelperForPassword(pwd: String, confirm: String) {
        val baseMsg = passwordRequirementsMessage(pwd)
        binding.tvPasswordHelper.text =
            if (confirm.isNotEmpty() && pwd != confirm) "Las contraseñas no coinciden" else baseMsg

        // Color dinámico (verde si todo OK)
        val strong = isStrongPassword(pwd) && (confirm.isEmpty() || pwd == confirm)
        val color = if (strong)
            resources.getColor(android.R.color.holo_green_dark, theme)
        else
            resources.getColor(R.color.dark_purple, theme)
        binding.tvPasswordHelper.setTextColor(color)
    }

    private fun passwordRequirementsMessage(pwd: String): String {
        val hasLen = pwd.length >= 6
        val hasUpper = pwd.any { it.isUpperCase() }
        val hasLetter = pwd.any { it.isLetter() }
        val hasDigit = pwd.any { it.isDigit() }
        val hasSpecial = pwd.any { !it.isLetterOrDigit() }

        return if (hasLen && hasUpper && hasLetter && hasDigit && hasSpecial) {
            "Contraseña fuerte ✅"
        } else buildString {
            append("Tu contraseña debe tener:\n")
            append(if (hasLen) "✅ " else "❌ "); append("Mínimo 6 caracteres\n")
            append(if (hasUpper) "✅ " else "❌ "); append("Al menos 1 mayúscula (A-Z)\n")
            append(if (hasDigit) "✅ " else "❌ "); append("Al menos 1 número (0-9)\n")
            append(if (hasSpecial) "✅ " else "❌ "); append("Al menos 1 carácter especial (!@#\$%&...)\n")
            append(if (hasLetter) "✅ " else "❌ "); append("Letras (a-z)\n")
        }
    }

    private fun isStrongPassword(password: String): Boolean {
        if (password.length < 6) return false
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        return hasUppercase && hasLetter && hasDigit && hasSpecial
    }

    // 🔧 FIX: ahora recibe EditText (sí tiene setSelection)
    private fun togglePasswordVisibility(editText: EditText, show: Boolean) {
        if (show) {
            editText.transformationMethod = null
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.text?.length ?: 0)
    }

    // ======= Diálogos legales =======
    private fun mostrarTerminos() {
        val texto = obtenerTextoTerminos()
        val scroll = ScrollView(this).apply {
            setPadding(32, 24, 32, 24)
            addView(TextView(this@RegisterActivity).apply {
                text = texto
                setTextColor(resources.getColor(R.color.dark_purple, theme))
            })
        }
        AlertDialog.Builder(this)
            .setTitle("Términos y Condiciones")
            .setView(scroll)
            .setPositiveButton("Aceptar") { d, _ ->
                binding.cbAceptarTyC.isChecked = true
                d.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarPrivacidad() {
        val texto = obtenerTextoPrivacidad()
        val scroll = ScrollView(this).apply {
            setPadding(32, 24, 32, 24)
            addView(TextView(this@RegisterActivity).apply {
                text = texto
                setTextColor(resources.getColor(R.color.dark_purple, theme))
            })
        }
        AlertDialog.Builder(this)
            .setTitle("Política de Privacidad")
            .setView(scroll)
            .setPositiveButton("Aceptar") { d, _ -> d.dismiss() }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun obtenerTextoTerminos(): String = """
Última actualización: 17 de octubre de 2025

1) Objeto del servicio
Esta aplicación facilita el registro de usuarios y la gestión de recordatorios de medicación, contactos (p. ej., familiar/cuidador) y notificaciones. No proporciona diagnóstico médico ni sustituye la consulta con profesionales de la salud.

2) Cuenta y verificación
Para usar el servicio debes crear una cuenta, aportar información veraz y mantenerla actualizada. Te enviaremos un correo de verificación; podremos suspender o cerrar cuentas no verificadas o que incumplan estos términos.

3) Uso permitido
Te comprometes a usar la app de forma lícita, sin vulnerar derechos de terceros, sin intentar acceder indebidamente a datos o sistemas, y sin afectar la disponibilidad o seguridad del servicio.

4) Contenido y datos ingresados
Los datos que registres (p. ej., nombre, cédula, EPS, RH, horarios, notas) son de tu titularidad. Eres responsable de su veracidad, exactitud y actualización. No ingreses datos sensibles de terceros sin su autorización.

5) Notificaciones y recordatorios
Las notificaciones dependen de la configuración del dispositivo y la conectividad. No garantizamos la entrega en todo momento. Debes confirmar tus tomas y mantener métodos alternos de respaldo (alarmas del sistema, apoyo de tu familiar/cuidador).

6) Exención de responsabilidad médica
La app NO es un dispositivo médico ni reemplaza indicaciones profesionales. Ante emergencias o dudas sobre tu tratamiento, consulta a tu médico o a los servicios de urgencias. El uso de la app es bajo tu propio riesgo.

7) Privacidad y seguridad
Tratamos tu información conforme a la Política de Privacidad. Aplicamos medidas de seguridad razonables, pero ningún sistema es 100 % infalible. Resguarda tus credenciales y notifica accesos no autorizados.

8) Edad mínima
Declaras tener la mayoría de edad aplicable o contar con autorización de tu representante legal. Si actúas como familiar/cuidador, garantizas tener legitimación para administrar datos del paciente.

9) Licencia y propiedad intelectual
Te otorgamos una licencia limitada, no exclusiva e intransferible para usar la app. Queda prohibida su ingeniería inversa, copia o uso comercial no autorizado. Las marcas y logotipos son de sus respectivos titulares.

10) Suspensión y terminación
Podemos suspender o terminar el servicio si incumples estos términos, por razones de seguridad, mantenimiento o por requerimientos legales.

11) Modificaciones
Podremos actualizar estos términos; publicaremos la versión vigente en la app. El uso continuado implicará tu aceptación.

12) Ley aplicable y contacto
Estos términos se rigen por la legislación aplicable en Colombia. Para consultas o reclamos: soporte@tuapp.com
""".trimIndent()

    private fun obtenerTextoPrivacidad(): String = """
Última actualización: 17 de octubre de 2025

1) Responsable del tratamiento
El responsable del tratamiento de tus datos es el titular de esta aplicación (contacto: soporte@tuapp.com).

2) Datos que tratamos
Datos de cuenta (nombre, correo, usuario), identificación (cédula), EPS, RH, configuración de recordatorios, logs técnicos y metadatos de uso. No solicitamos historiales clínicos ni diagnósticos.

3) Finalidades
Crear y administrar tu cuenta; enviar recordatorios y notificaciones; permitir la interacción con familiar/cuidador; mejorar la calidad del servicio; cumplir obligaciones legales.

4) Base jurídica
Tu consentimiento y la necesidad de ejecutar el servicio solicitado. Puedes revocar tu consentimiento en cualquier momento sin efectos retroactivos.

5) Destinatarios
Proveedores tecnológicos (p. ej., servicios en la nube) bajo acuerdos de confidencialidad. No vendemos tus datos.

6) Transferencias internacionales
Podría existir procesamiento fuera de tu país conforme a cláusulas contractuales adecuadas.

7) Conservación
Mientras exista tu cuenta y por los plazos necesarios para obligaciones legales o defensa de reclamaciones.

8) Derechos
Acceso, rectificación, actualización, supresión y oposición. Para ejercerlos escribe a soporte@tuapp.com. Responderemos en los plazos legales.

9) Seguridad
Aplicamos medidas técnicas y organizativas razonables. Aun así, no podemos garantizar seguridad absoluta.

10) Cambios
Publicaremos cualquier cambio en esta política dentro de la app.
""".trimIndent()

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
