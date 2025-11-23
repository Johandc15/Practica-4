package com.ucentral.proyectopractica3.model

data class HistorialItem(
    val nombreMedicamento: String = "",
    val totalRecordatorios: Int = 0,
    val tomados: Int = 0,
    val porcentaje: Int = 0,
    val horasTomadas: List<String> = emptyList()
)
