package com.ucentral.proyectopractica3.utils

object PacienteSeleccionado {
    var pacienteId: String? = null
    var pacienteNombre: String? = null

    fun limpiar() {
        pacienteId = null
        pacienteNombre = null
    }
}
