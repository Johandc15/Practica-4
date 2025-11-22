package com.ucentral.proyectopractica3.ui.familiar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class AgregarMedicamentoFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_agregar_medicamento_familiar,
            container,
            false
        )

        db = FirebaseFirestore.getInstance()

        // ----- Referencias UI -----
        val etNombre = view.findViewById<EditText>(R.id.etNombreMedicamentoFamiliar)
        val etNombreGenerico = view.findViewById<EditText>(R.id.etNombreGenericoFamiliar)

        val spPresentacion = view.findViewById<Spinner>(R.id.spPresentacionFamiliar)
        val spVia = view.findViewById<Spinner>(R.id.spViaAdministracionFamiliar)

        val etDosisPorToma = view.findViewById<EditText>(R.id.etDosisPorTomaFamiliar)
        val spUnidadDosis = view.findViewById<Spinner>(R.id.spUnidadDosisFamiliar)

        val etCantidadTotal = view.findViewById<EditText>(R.id.etCantidadTotalFamiliar)
        val spUnidadCantidadTotal = view.findViewById<Spinner>(R.id.spUnidadCantidadTotalFamiliar)

        val spTomasPorDia = view.findViewById<Spinner>(R.id.spTomasPorDiaFamiliar)
        val cbManana = view.findViewById<CheckBox>(R.id.cbMananaFamiliar)
        val cbMediodia = view.findViewById<CheckBox>(R.id.cbMediodiaFamiliar)
        val cbTarde = view.findViewById<CheckBox>(R.id.cbTardeFamiliar)
        val cbNoche = view.findViewById<CheckBox>(R.id.cbNocheFamiliar)

        val etFechaInicio = view.findViewById<EditText>(R.id.etFechaInicioFamiliar)
        val etFechaFin = view.findViewById<EditText>(R.id.etFechaFinFamiliar)
        val cbUsoCronico = view.findViewById<CheckBox>(R.id.cbUsoCronicoFamiliar)

        val etNotas = view.findViewById<EditText>(R.id.etNotasFamiliar)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarMedicamentoFamiliar)

        // ----- Adapters para Spinners -----
        val presentaciones = listOf(
            "Selecciona presentación",
            "Tabletas",
            "Cápsulas",
            "Gotas",
            "Jarabe",
            "Inyectable",
            "Inhalador"
        )
        spPresentacion.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            presentaciones
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val vias = listOf(
            "Selecciona vía de administración",
            "Oral",
            "Sublingual",
            "Tópica",
            "Inhalada",
            "Oftálmica",
            "Intramuscular",
            "Subcutánea"
        )
        spVia.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            vias
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val unidadesDosis = listOf("tableta(s)", "cápsula(s)", "gota(s)", "ml", "mg", "UI")
        spUnidadDosis.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            unidadesDosis
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val unidadesCantidad = listOf("tabletas", "cápsulas", "ml", "frascos", "parches")
        spUnidadCantidadTotal.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            unidadesCantidad
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val tomasDia = (1..4).map { "$it toma(s) por día" }
        spTomasPorDia.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tomasDia
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Si marca uso crónico, deshabilitamos la fecha fin
        cbUsoCronico.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etFechaFin.text.clear()
                etFechaFin.isEnabled = false
            } else {
                etFechaFin.isEnabled = true
            }
        }

        // ----- Guardar medicamento -----
        btnGuardar.setOnClickListener {
            val pacienteId = PacienteSeleccionado.pacienteId
            if (pacienteId == null) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona primero un paciente",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val nombre = etNombre.text.toString().trim()
            val nombreGenerico = etNombreGenerico.text.toString().trim()
            val presentacionSel = spPresentacion.selectedItem.toString()
            val viaSel = spVia.selectedItem.toString()

            val dosisPorToma = etDosisPorToma.text.toString().trim()
            val unidadDosisSel = spUnidadDosis.selectedItem.toString()

            val cantidadTotal = etCantidadTotal.text.toString().trim()
            val unidadCantSel = spUnidadCantidadTotal.selectedItem.toString()

            val tomasPorDiaIndex = spTomasPorDia.selectedItemPosition
            val tomasPorDia = tomasPorDiaIndex + 1

            val fechaInicio = etFechaInicio.text.toString().trim()
            val fechaFin = etFechaFin.text.toString().trim()
            val usoCronico = cbUsoCronico.isChecked

            val notas = etNotas.text.toString().trim()

            // ----- Validaciones básicas -----
            if (nombre.isEmpty() ||
                dosisPorToma.isEmpty() ||
                cantidadTotal.isEmpty() ||
                fechaInicio.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Completa los campos obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (presentacionSel.startsWith("Selecciona") ||
                viaSel.startsWith("Selecciona")
            ) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona presentación y vía de administración",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!cbManana.isChecked && !cbMediodia.isChecked &&
                !cbTarde.isChecked && !cbNoche.isChecked
            ) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona al menos un ciclo (mañana, tarde, etc.)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Resúmenes antiguos para no romper otras pantallas (dosis, cantidad, horario)
            val dosisTexto = "$dosisPorToma $unidadDosisSel"
            val cantidadTexto = "$cantidadTotal $unidadCantSel"

            val ciclosTexto = buildString {
                if (cbManana.isChecked) append("Mañana ")
                if (cbMediodia.isChecked) append("Mediodía ")
                if (cbTarde.isChecked) append("Tarde ")
                if (cbNoche.isChecked) append("Noche ")
            }.trim()

            val id = UUID.randomUUID().toString()

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

                // Campos resumen antiguos para compatibilidad
                dosis = dosisTexto,
                cantidad = cantidadTexto,
                horario = ciclosTexto
            )

            db.collection("usuarios").document(pacienteId)
                .collection("medicamentos").document(medicamento.id)
                .set(medicamento)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Medicamento agregado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

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
                    Toast.makeText(
                        requireContext(),
                        "Error al guardar medicamento",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        return view
    }
}
