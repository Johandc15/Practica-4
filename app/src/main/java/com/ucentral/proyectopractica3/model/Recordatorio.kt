package com.ucentral.proyectopractica3.model

data class Recordatorio(
    var id: String = "",
    var medicamentoId: String = "",
    var nombreMedicamento: String = "",
    var hora: String = "",
    var repeticiones: Int = 1,
    var tomado: Boolean = false,
    var fechaRegistro: Long = System.currentTimeMillis()
)
