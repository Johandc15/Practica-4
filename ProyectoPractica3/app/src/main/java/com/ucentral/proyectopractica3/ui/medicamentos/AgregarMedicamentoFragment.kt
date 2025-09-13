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

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etDosis = view.findViewById<EditText>(R.id.etDosis)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val etHorario = view.findViewById<EditText>(R.id.etHorario)
        val etNotas = view.findViewById<EditText>(R.id.etNotas)
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardarMedicamento)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val dosis = etDosis.text.toString()
            val cantidad = etCantidad.text.toString()
            val horario = etHorario.text.toString()
            val notas = etNotas.text.toString()

            if (nombre.isNotEmpty() && dosis.isNotEmpty() && cantidad.isNotEmpty()) {
                val uid = auth.currentUser?.uid ?: return@setOnClickListener
                val id = UUID.randomUUID().toString()
                val medicamento = Medicamento(id, nombre, dosis, cantidad, horario, notas)

                db.collection("usuarios").document(uid)
                    .collection("medicamentos").document(id)
                    .set(medicamento)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Medicamento guardado", Toast.LENGTH_SHORT).show()
                        etNombre.text.clear()
                        etDosis.text.clear()
                        etCantidad.text.clear()
                        etHorario.text.clear()
                        etNotas.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
