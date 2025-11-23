// src/main/java/com/ucentral/proyectopractica3/ui/recordatorios/ProgramarRecordatoriosFragment.kt
package com.ucentral.proyectopractica3.ui.recordatorios

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.model.Recordatorio
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmChannels
import com.ucentral.proyectopractica3.ui.notificaciones.AlarmScheduler
import com.ucentral.proyectopractica3.ui.notificaciones.ExactAlarmHelper
import com.ucentral.proyectopractica3.ui.notificaciones.PermissionUtils
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class ProgramarRecordatoriosFragment : Fragment() {

    companion object {
        private const val ARG_PACIENTE_ID = "pacienteId"
        private const val ARG_MEDICAMENTO_ID = "medicamentoId"

        fun newInstance(pacienteId: String? = null, medicamentoId: String? = null): ProgramarRecordatoriosFragment {
            val f = ProgramarRecordatoriosFragment()
            f.arguments = Bundle().apply {
                if (pacienteId != null) putString(ARG_PACIENTE_ID, pacienteId)
                if (medicamentoId != null) putString(ARG_MEDICAMENTO_ID, medicamentoId)
            }
            return f
        }
    }

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // UI
    private lateinit var tvContexto: TextView
    private lateinit var spMedicamentos: Spinner
    private lateinit var rgModo: RadioGroup

    // Contenedores por modo
    private lateinit var modoCiclos: View
    private lateinit var modoIntervalo: View
    private lateinit var modoManual: View

    // Modo CICLOS
    private lateinit var cbManana: CheckBox
    private lateinit var cbMediodia: CheckBox
    private lateinit var cbTarde: CheckBox
    private lateinit var cbNoche: CheckBox
    private lateinit var btnHoraManana: Button
    private lateinit var btnHoraMediodia: Button
    private lateinit var btnHoraTarde: Button
    private lateinit var btnHoraNoche: Button

    // Modo CADA_X_HORAS
    private lateinit var btnHoraInicio: Button
    private lateinit var etIntervaloHoras: EditText

    // Modo MANUAL
    private lateinit var btnAgregarHoraManual: Button
    private lateinit var lvHorasManual: ListView
    private val horasManual = mutableListOf<String>()
    private lateinit var adapterHorasManual: ArrayAdapter<String>

    // Rango de fechas
    private lateinit var btnFechaInicio: Button
    private lateinit var etDiasDuracion: EditText

    // Acciones
    private lateinit var btnPrevisualizar: Button
    private lateinit var tvPreview: TextView
    private lateinit var btnGuardar: Button

    // Datos cargados
    private var pacienteIdArg: String? = null
    private var medicamentoIdArg: String? = null
    private var pacienteIdFinal: String? = null
    private var medicamentos: List<Medicamento> = emptyList()
    private var medicamentoFijado: Medicamento? = null

    // Helpers de hora
    private var horaManana = "08:00"
    private var horaMediodia = "12:00"
    private var horaTarde = "16:00"
    private var horaNoche = "20:00"
    private var horaInicioIntervalo = "08:00"

    private val sdfFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val sdfHora = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_programar_recordatorios, container, false)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        pacienteIdArg = arguments?.getString(ARG_PACIENTE_ID)
        medicamentoIdArg = arguments?.getString(ARG_MEDICAMENTO_ID)

        // === Referencias UI ===
        tvContexto = v.findViewById(R.id.tvContextoSeleccion)
        spMedicamentos = v.findViewById(R.id.spMedicamentosProgramador)
        rgModo = v.findViewById(R.id.rgModoProgramacion)

        modoCiclos = v.findViewById(R.id.groupCiclos)
        modoIntervalo = v.findViewById(R.id.groupCadaXHoras)
        modoManual = v.findViewById(R.id.groupManual)

        cbManana = v.findViewById(R.id.cbMananaProg)
        cbMediodia = v.findViewById(R.id.cbMediodiaProg)
        cbTarde = v.findViewById(R.id.cbTardeProg)
        cbNoche = v.findViewById(R.id.cbNocheProg)

        btnHoraManana = v.findViewById(R.id.btnHoraManana)
        btnHoraMediodia = v.findViewById(R.id.btnHoraMediodia)
        btnHoraTarde = v.findViewById(R.id.btnHoraTarde)
        btnHoraNoche = v.findViewById(R.id.btnHoraNoche)

        btnHoraInicio = v.findViewById(R.id.btnHoraInicio)
        etIntervaloHoras = v.findViewById(R.id.etIntervaloHoras)

        btnAgregarHoraManual = v.findViewById(R.id.btnAgregarHoraManual)
        lvHorasManual = v.findViewById(R.id.lvHorasManual)
        adapterHorasManual = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, horasManual)
        lvHorasManual.adapter = adapterHorasManual
        lvHorasManual.setOnItemLongClickListener { _, _, pos, _ ->
            horasManual.removeAt(pos)
            adapterHorasManual.notifyDataSetChanged()
            true
        }

        btnFechaInicio = v.findViewById(R.id.btnFechaInicio)
        etDiasDuracion = v.findViewById(R.id.etDiasDuracion)

        btnPrevisualizar = v.findViewById(R.id.btnPrevisualizar)
        tvPreview = v.findViewById(R.id.tvPreview)
        btnGuardar = v.findViewById(R.id.btnGuardarProgramacion)

        // Listeners de modo
        rgModo.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCiclos -> {
                    modoCiclos.isVisible = true
                    modoIntervalo.isVisible = false
                    modoManual.isVisible = false
                }
                R.id.rbCadaXHoras -> {
                    modoCiclos.isVisible = false
                    modoIntervalo.isVisible = true
                    modoManual.isVisible = false
                }
                R.id.rbManual -> {
                    modoCiclos.isVisible = false
                    modoIntervalo.isVisible = false
                    modoManual.isVisible = true
                }
            }
            tvPreview.text = ""
        }

        // Hora pickers
        btnHoraManana.setOnClickListener { pickHora { h -> horaManana = h; btnHoraManana.text = "Mañana: $h" } }
        btnHoraMediodia.setOnClickListener { pickHora { h -> horaMediodia = h; btnHoraMediodia.text = "Mediodía: $h" } }
        btnHoraTarde.setOnClickListener { pickHora { h -> horaTarde = h; btnHoraTarde.text = "Tarde: $h" } }
        btnHoraNoche.setOnClickListener { pickHora { h -> horaNoche = h; btnHoraNoche.text = "Noche: $h" } }

        btnHoraInicio.setOnClickListener { pickHora { h -> horaInicioIntervalo = h; btnHoraInicio.text = "Hora inicio: $h" } }
        btnAgregarHoraManual.setOnClickListener { pickHora { h -> horasManual.add(h); adapterHorasManual.notifyDataSetChanged() } }

        // Fecha inicio
        btnFechaInicio.setOnClickListener {
            val hoy = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    val cal = Calendar.getInstance().apply { set(y, m, d, 0, 0, 0) }
                    btnFechaInicio.text = sdfFecha.format(cal.time)
                },
                hoy.get(Calendar.YEAR),
                hoy.get(Calendar.MONTH),
                hoy.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnPrevisualizar.setOnClickListener { previsualizar() }
        btnGuardar.setOnClickListener { guardar() }

        // Cargar paciente/medicamento contexto
        resolverContextoYcargar()
        return v
    }

    private fun resolverContextoYcargar() {
        // 1) Resolver paciente destino
        pacienteIdFinal = pacienteIdArg
            ?: PacienteSeleccionado.pacienteId
                    ?: auth.currentUser?.uid

        val nombreCtx = PacienteSeleccionado.pacienteNombre
        tvContexto.text = if (pacienteIdArg != null || PacienteSeleccionado.pacienteId != null) {
            "Paciente seleccionado: ${nombreCtx ?: "(sin nombre)"}"
        } else {
            "Paciente actual (tú mismo)"
        }

        // 2) Cargar medicamentos
        val pid = pacienteIdFinal ?: return
        db.collection("usuarios").document(pid).collection("medicamentos")
            .get()
            .addOnSuccessListener { snap ->
                medicamentos = snap.map { it.toObject(Medicamento::class.java) }

                if (medicamentoIdArg != null) {
                    medicamentoFijado = medicamentos.firstOrNull { it.id == medicamentoIdArg }
                    if (medicamentoFijado == null) {
                        Toast.makeText(requireContext(), "Medicamento no encontrado", Toast.LENGTH_SHORT).show()
                    }
                    spMedicamentos.isEnabled = medicamentoFijado == null
                }

                val nombres = medicamentos.map { it.nombre }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                spMedicamentos.adapter = adapter

                // Seleccionar el fijado si venía por arg
                medicamentoFijado?.let { fijado ->
                    val idx = medicamentos.indexOfFirst { it.id == fijado.id }
                    if (idx >= 0) spMedicamentos.setSelection(idx)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar medicamentos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun pickHora(onPicked: (String) -> Unit) {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, h, m -> onPicked(String.format("%02d:%02d", h, m)) },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun previsualizar() {
        val pid = pacienteIdFinal ?: run {
            Toast.makeText(requireContext(), "Selecciona un paciente", Toast.LENGTH_SHORT).show(); return
        }
        if (medicamentos.isEmpty()) {
            Toast.makeText(requireContext(), "No hay medicamentos disponibles", Toast.LENGTH_SHORT).show(); return
        }
        val med = medicamentoFijado ?: medicamentos.getOrNull(spMedicamentos.selectedItemPosition)
        ?: run { Toast.makeText(requireContext(), "Selecciona un medicamento", Toast.LENGTH_SHORT).show(); return }

        val fechaInicioStr = btnFechaInicio.text.toString().takeIf { it.contains("/") }
            ?: run { Toast.makeText(requireContext(), "Elige fecha de inicio", Toast.LENGTH_SHORT).show(); return }
        val dias = etDiasDuracion.text.toString().toIntOrNull() ?: run {
            Toast.makeText(requireContext(), "Ingresa días de duración", Toast.LENGTH_SHORT).show(); return
        }
        if (dias <= 0) {
            Toast.makeText(requireContext(), "Los días deben ser ≥ 1", Toast.LENGTH_SHORT).show(); return
        }

        val horasPorDia = when (rgModo.checkedRadioButtonId) {
            R.id.rbCiclos -> buildList {
                if (cbManana.isChecked) add(horaManana)
                if (cbMediodia.isChecked) add(horaMediodia)
                if (cbTarde.isChecked) add(horaTarde)
                if (cbNoche.isChecked) add(horaNoche)
            }
            R.id.rbCadaXHoras -> {
                val intervalo = etIntervaloHoras.text.toString().toIntOrNull() ?: 0
                if (intervalo <= 0) {
                    Toast.makeText(requireContext(), "Intervalo inválido", Toast.LENGTH_SHORT).show()
                    return
                }
                generarHorasCadaX(horaInicioIntervalo, intervalo)
            }
            R.id.rbManual -> horasManual.sorted()
            else -> emptyList()
        }

        if (horasPorDia.isEmpty()) {
            Toast.makeText(requireContext(), "Selecciona/añade al menos una hora", Toast.LENGTH_SHORT).show()
            return
        }

        val lista = expandirRango(fechaInicioStr, dias, horasPorDia)
        tvPreview.text = lista.joinToString("\n") { it }
    }

    private fun guardar() {
        val pid = pacienteIdFinal ?: run {
            Toast.makeText(requireContext(), "Selecciona un paciente", Toast.LENGTH_SHORT).show(); return
        }
        if (medicamentos.isEmpty()) {
            Toast.makeText(requireContext(), "No hay medicamentos disponibles", Toast.LENGTH_SHORT).show(); return
        }
        val med = medicamentoFijado ?: medicamentos.getOrNull(spMedicamentos.selectedItemPosition)
        ?: run { Toast.makeText(requireContext(), "Selecciona un medicamento", Toast.LENGTH_SHORT).show(); return }

        val fechaInicioStr = btnFechaInicio.text.toString().takeIf { it.contains("/") }
            ?: run { Toast.makeText(requireContext(), "Elige fecha de inicio", Toast.LENGTH_SHORT).show(); return }
        val dias = etDiasDuracion.text.toString().toIntOrNull() ?: run {
            Toast.makeText(requireContext(), "Ingresa días de duración", Toast.LENGTH_SHORT).show(); return
        }
        if (dias <= 0) {
            Toast.makeText(requireContext(), "Los días deben ser ≥ 1", Toast.LENGTH_SHORT).show(); return
        }

        val modo = when (rgModo.checkedRadioButtonId) {
            R.id.rbCiclos -> "CICLOS"
            R.id.rbCadaXHoras -> "CADA_X_HORAS"
            R.id.rbManual -> "MANUAL"
            else -> "CICLOS"
        }

        val horasPorDia = when (modo) {
            "CICLOS" -> buildList {
                if (cbManana.isChecked) add(horaManana)
                if (cbMediodia.isChecked) add(horaMediodia)
                if (cbTarde.isChecked) add(horaTarde)
                if (cbNoche.isChecked) add(horaNoche)
            }
            "CADA_X_HORAS" -> {
                val intervalo = etIntervaloHoras.text.toString().toIntOrNull() ?: 0
                if (intervalo <= 0) {
                    Toast.makeText(requireContext(), "Intervalo inválido", Toast.LENGTH_SHORT).show()
                    return
                }
                generarHorasCadaX(horaInicioIntervalo, intervalo)
            }
            else -> horasManual.sorted()
        }

        if (horasPorDia.isEmpty()) {
            Toast.makeText(requireContext(), "Selecciona/añade al menos una hora", Toast.LENGTH_SHORT).show()
            return
        }

        // Expandir y guardar
        val lista = expandirRango(fechaInicioStr, dias, horasPorDia)

        // Asegurar permisos/canales antes de programar
        AlarmChannels.ensureCreated(requireContext())
        PermissionUtils.ensurePostNotificationPermission(requireActivity())
        ExactAlarmHelper.ensureExactAlarmAllowed(requireContext())

        val col = db.collection("usuarios").document(pid).collection("recordatorios")
        val batch = db.batch()

        lista.forEach { fechaHora ->
            val id = UUID.randomUUID().toString()
            val hora = fechaHora.substringAfter(" ") // "HH:mm"
            // Guardamos compatible con tu Recordatorio + metadatos en map extra
            val base = Recordatorio(
                id = id,
                medicamentoId = med.id,
                nombreMedicamento = med.nombre,
                hora = hora,
                repeticiones = 1
            )

            val meta = mapOf(
                "modoProgramacion" to modo,
                "fecha" to fechaHora.substringBefore(" "), // dd/MM/yyyy
                "pacienteId" to pid,
                "medicamentoNombre" to med.nombre
            )

            val docRef = col.document(id)
            batch.set(docRef, base)
            batch.update(docRef, meta)

            // Programar alarma exacta para cada uno (diaria a esa hora):
            val (h, m) = hora.split(":").map { it.toInt() }
            AlarmScheduler.scheduleDailyExact(
                context = requireContext(),
                recordatorioId = id,
                nombreMedicamento = med.nombre,
                hora24 = h,
                minuto = m
            )
        }

        batch.commit()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Recordatorios programados", Toast.LENGTH_SHORT).show()
                tvPreview.text = ""
                // Limpieza mínima
                horasManual.clear(); adapterHorasManual.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar programaciones", Toast.LENGTH_SHORT).show()
            }
    }

    private fun generarHorasCadaX(horaInicio: String, intervalo: Int): List<String> {
        // Genera horas dentro de un día (00:00-23:59) empezando en horaInicio, saltando de 'intervalo' horas
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, horaInicio.substring(0,2).toInt())
            set(Calendar.MINUTE, horaInicio.substring(3,5).toInt())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val res = mutableListOf<String>()
        val startH = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        for (k in 0 until (24 * 60) step (intervalo * 60)) {
            val total = startH + k
            if (total >= 24 * 60) break
            val h = total / 60
            val m = total % 60
            res.add(String.format("%02d:%02d", h, m))
        }
        return res
    }

    private fun expandirRango(fechaInicioStr: String, dias: Int, horasPorDia: List<String>): List<String> {
        val cal = Calendar.getInstance()
        cal.time = sdfFecha.parse(fechaInicioStr)!!
        val out = mutableListOf<String>()
        repeat(dias) {
            val fecha = sdfFecha.format(cal.time) // dd/MM/yyyy
            horasPorDia.forEach { h ->
                out.add("$fecha $h")
            }
            cal.add(Calendar.DATE, 1)
        }
        return out
    }
}
