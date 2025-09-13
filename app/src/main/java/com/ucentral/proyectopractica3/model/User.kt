package com.ucentral.proyectopractica3.model

import androidx.credentials.PasswordCredential

data class User(
    val uid: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val usuario: String = "",
    val correo: String = "",
    val contraseña: String,
    val tipoUsuario: String = ""
)