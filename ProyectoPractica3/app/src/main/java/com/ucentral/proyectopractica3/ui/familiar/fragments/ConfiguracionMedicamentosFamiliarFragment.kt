package com.ucentral.proyectopractica3.ui.familiar.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.ucentral.proyectopractica3.R
import com.ucentral.proyectopractica3.model.Medicamento
import com.ucentral.proyectopractica3.utils.PacienteSeleccionado
import com.google.firebase.firestore.FirebaseFirestore

class ConfiguracionMedicamentosFamiliarFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var layoutConfiguracion: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_configuracion_medicamentos_familiar, container, false)

        db = FirebaseFirestore.getInstance()
        layoutConfiguracion = view.findViewById(R.id.layoutConfiguracionMedicamentosFamiliar)

        cargarMedicamentos()

        return view
    }

    private fun cargarMedicamentos() {
        val pacienteId = PacienteSeleccionado.pacienteId ?: return
        db.collection("usuarios").document(pacienteId)
            .collection("medicamentos")
            .get()
            .addOnSuccessListener { result ->
                layoutConfiguracion.removeAllViews()

                for (doc in result) {
                    val med = doc.toObject(Medicamento::class.java)
                    val id = doc.id

                    val contenedor = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(16)
                        setBackgroundResource(R.drawable.border_gray)
                    }

                    val texto = TextView(requireContext()).apply {
                        text = "${med.nombre}\nDosis: ${med.dosis} | Cantidad: ${med.cantidad}"
                    }

                    val btnEliminar = Button(requireContext()).apply {
                        text = "Eliminar"
                        setOnClickListener {
                            db.collection("usuarios").document(pacienteId)
                                .collection("medicamentos").document(id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Eliminado", Toast.LENGTH_SHORT).show()
                                    cargarMedicamentos()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }

                    contenedor.addView(texto)
                    contenedor.addView(btnEliminar)
                    layoutConfiguracion.addView(contenedor)
                }
            }
    }
}
