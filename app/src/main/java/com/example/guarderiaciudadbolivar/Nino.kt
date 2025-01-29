package com.example.guarderiaciudadbolivar

import java.util.Date

data class Nino(
    val noMatricula: Int,
    val nombre: String,
    val acudienteCedula: Int,
    val fechaNacimiento: Date?,
    val fechaIngreso: Date?,
    val fechaFin: Date?,
    val estado: String?
)

