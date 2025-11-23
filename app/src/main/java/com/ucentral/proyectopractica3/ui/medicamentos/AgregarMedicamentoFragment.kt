package com.ucentral.proyectopractica3.ui.medicamentos

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import java.util.*

class AgregarMedicamentoFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_agregar_medicamento, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // ----- Referencias UI -----
        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etNombreGenerico = view.findViewById<EditText>(R.id.etNombreGenerico)
        val spPresentacion = view.findViewById<Spinner>(R.id.spPresentacion)
        val spVia = view.findViewById<Spinner>(R.id.spViaAdministracion)

        val etDosisPorToma = view.findViewById<EditText>(R.id.etDosisPorToma)
        val spUnidadDosis = view.findViewById<Spinner>(R.id.spUnidadDosis)

        val etCantidadTotal = view.findViewById<EditText>(R.id.etCantidadTotal)
        val spUnidadCantidadTotal = view.findViewById<Spinner>(R.id.spUnidadCantidadTotal)

        val spTomasPorDia = view.findViewById<Spinner>(R.id.spTomasPorDia)
        val cbManana = view.findViewById<CheckBox>(R.id.cbManana)
        val cbMediodia = view.findViewById<CheckBox>(R.id.cbMediodia)
        val cbTarde = view.findViewById<CheckBox>(R.id.cbTarde)
        val cbNoche = view.findViewById<CheckBox>(R.id.cbNoche)

        val etFechaInicio = view.findViewById<EditText>(R.id.etFechaInicio)
        val etFechaFin = view.findViewById<EditText>(R.id.etFechaFin)
        val cbUsoCronico = view.findViewById<CheckBox>(R.id.cbUsoCronico)

        val etNotas = view.findViewById<EditText>(R.id.etNotas)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarMedicamento)

        // ----- Adapters para los Spinners -----
        val presentaciones = listOf("Selecciona presentación", "Tabletas", "Cápsulas", "Gotas", "Jarabe", "Inyectable", "Inhalador")
        spPresentacion.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, presentaciones).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val vias = listOf("Selecciona vía de administración", "Oral", "Sublingual", "Tópica", "Inhalada", "Oftálmica", "Intramuscular", "Subcutánea")
        spVia.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, vias).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val unidadesDosis = listOf("tableta(s)", "cápsula(s)", "gota(s)", "ml", "mg", "UI")
        spUnidadDosis.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unidadesDosis).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val unidadesCantidad = listOf("tabletas", "cápsulas", "ml", "frascos", "parches")
        spUnidadCantidadTotal.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unidadesCantidad).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val tomasDia = (1..4).map { "$it toma(s) por día" }
        spTomasPorDia.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tomasDia).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Si marca uso crónico, limpiamos/ignoramos fecha fin
        cbUsoCronico.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etFechaFin.text.clear()
                etFechaFin.isEnabled = false
            } else {
                etFechaFin.isEnabled = true
            }
        }

        // ----- Guardar -----
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val nombreGenerico = etNombreGenerico.text.toString().trim()
            val presentacionSel = spPresentacion.selectedItem.toString()
            val viaSel = spVia.selectedItem.toString()

            val dosisPorToma = etDosisPorToma.text.toString().trim()
            val unidadDosisSel = spUnidadDosis.selectedItem.toString()

            val cantidadTotal = etCantidadTotal.text.toString().trim()
            val unidadCantSel = spUnidadCantidadTotal.selectedItem.toString()

            val tomasPorDiaIndex = spTomasPorDia.selectedItemPosition
            val tomasPorDia = tomasPorDiaIndex + 1 // porque el rango 1..4 se mapea así

            val fechaInicio = etFechaInicio.text.toString().trim()
            val fechaFin = etFechaFin.text.toString().trim()
            val usoCronico = cbUsoCronico.isChecked

            val notas = etNotas.text.toString().trim()

            // Validaciones básicas
            if (nombre.isEmpty() ||
                dosisPorToma.isEmpty() ||
                cantidadTotal.isEmpty() ||
                fechaInicio.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (presentacionSel.startsWith("Selecciona") || viaSel.startsWith("Selecciona")) {
                Toast.makeText(requireContext(), "Selecciona presentación y vía de administración", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbManana.isChecked && !cbMediodia.isChecked && !cbTarde.isChecked && !cbNoche.isChecked) {
                Toast.makeText(requireContext(), "Selecciona al menos un ciclo (mañana, tarde, etc.)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            val id = UUID.randomUUID().toString()

            // Construimos los campos "texto resumen" antiguos para no romper las pantallas que usan dosis/cantidad/horario
            val dosisTexto = "$dosisPorToma $unidadDosisSel"
            val cantidadTexto = "$cantidadTotal $unidadCantSel"

            val ciclosTexto = buildString {
                if (cbManana.isChecked) append("Mañana ")
                if (cbMediodia.isChecked) append("Mediodía ")
                if (cbTarde.isChecked) append("Tarde ")
                if (cbNoche.isChecked) append("Noche ")
            }.trim()

            val medicamento = Medicamento(
                id = id,
                nombre = nombre,
                nombreGenerico = nombreGenerico,
                presentacion = presentacionSel,
                viaAdministracion = viaSel,

                dosisPorToma = dosisPorToma,
                unidadDosis = unidadDosisSel,

                tomasPorDia = tomasPorDia,
                cicloManana = cbManana.isChecked,
                cicloMediodia = cbMediodia.isChecked,
                cicloTarde = cbTarde.isChecked,
                cicloNoche = cbNoche.isChecked,

                cantidadTotal = cantidadTotal,
                unidadCantidadTotal = unidadCantSel,

                fechaInicio = fechaInicio,
                fechaFin = if (usoCronico) "" else fechaFin,
                usoCronico = usoCronico,

                notas = notas,

                // Campos resumen antiguos
                dosis = dosisTexto,
                cantidad = cantidadTexto,
                horario = ciclosTexto
            )

            db.collection("usuarios").document(uid)
                .collection("medicamentos").document(id)
                .set(medicamento)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Medicamento guardado", Toast.LENGTH_SHORT).show()

                    // Limpiar formulario
                    etNombre.text.clear()
                    etNombreGenerico.text.clear()
                    etDosisPorToma.text.clear()
                    etCantidadTotal.text.clear()
                    etFechaInicio.text.clear()
                    etFechaFin.text.clear()
                    etNotas.text.clear()

                    spPresentacion.setSelection(0)
                    spVia.setSelection(0)
                    spUnidadDosis.setSelection(0)
                    spUnidadCantidadTotal.setSelection(0)
                    spTomasPorDia.setSelection(0)

                    cbManana.isChecked = false
                    cbMediodia.isChecked = false
                    cbTarde.isChecked = false
                    cbNoche.isChecked = false
                    cbUsoCronico.isChecked = false
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}
