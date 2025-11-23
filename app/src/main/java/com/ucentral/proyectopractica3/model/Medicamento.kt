package com.ucentral.proyectopractica3.model

data class Medicamento(
    // Identificación básica
    var id: String = "",
    var nombre: String = "",                 // Nombre comercial
    var nombreGenerico: String = "",         // Opcional
    var presentacion: String = "",           // Tabletas, jarabe, gotas...
    var viaAdministracion: String = "",      // Oral, tópica, inhalada...

    // Posología estructurada
    var dosisPorToma: String = "",           // Solo el número (ej: "1" o "500")
    var unidadDosis: String = "",            // tableta(s), mg, ml, gotas...

    // Ciclos (en vez de un horario suelto)
    var tomasPorDia: Int = 1,
    var cicloManana: Boolean = false,
    var cicloMediodia: Boolean = false,
    var cicloTarde: Boolean = false,
    var cicloNoche: Boolean = false,

    // Cantidad total del tratamiento
    var cantidadTotal: String = "",          // Ej: "30"
    var unidadCantidadTotal: String = "",    // tabletas, ml, etc.

    // Duración del tratamiento
    var fechaInicio: String = "",            // Ej: "03/11/2025"
    var fechaFin: String = "",               // Vacío si uso crónico
    var usoCronico: Boolean = false,         // true = sin fecha de terminación

    // Notas / indicaciones
    var notas: String = "",                  // Instrucciones extra: con comida, etc.

    // ====== CAMPOS ANTIGUOS (para compatibilidad con datos ya guardados) ====== //
    var dosis: String = "",                  // Aquí guardaremos un resumen: "1 tableta (500 mg)"
    var cantidad: String = "",               // "30 tabletas"
    var horario: String = ""                 // "Mañana y noche" (a partir de los ciclos)
)
