package com.ucentral.proyectopractica3.ui.familiar.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AgregarMedicamentoFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_agregar_medicamento_familiar, container, false)

        db = FirebaseFirestore.getInstance()

        val etNombre = view.findViewById<EditText>(R.id.etNombreMedicamentoFamiliar)
        val etDosis = view.findViewById<EditText>(R.id.etDosisFamiliar)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidadFamiliar)
        val etHorario = view.findViewById<EditText>(R.id.etHorarioFamiliar)
        val etNotas = view.findViewById<EditText>(R.id.etNotasFamiliar)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarMedicamentoFamiliar)

        btnGuardar.setOnClickListener {
            val pacienteId = PacienteSeleccionado.pacienteId
            if (pacienteId == null) {
                Toast.makeText(requireContext(), "Selecciona primero un paciente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val medicamento = Medicamento(
                id = UUID.randomUUID().toString(),
                nombre = etNombre.text.toString(),
                dosis = etDosis.text.toString(),
                cantidad = etCantidad.text.toString(),
                horario = etHorario.text.toString(),
                notas = etNotas.text.toString()
            )

            db.collection("usuarios").document(pacienteId)
                .collection("medicamentos").document(medicamento.id)
                .set(medicamento)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Medicamento agregado correctamente", Toast.LENGTH_SHORT).show()
                    etNombre.text.clear()
                    etDosis.text.clear()
                    etCantidad.text.clear()
                    etHorario.text.clear()
                    etNotas.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar medicamento", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}
