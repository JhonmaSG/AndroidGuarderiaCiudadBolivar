package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallesAlergias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_alergias)

        // Obtener referencias de los TextViews
        val txtNoMatricula = findViewById<TextView>(R.id.txtNoMatricula)
        val txtIngrediente = findViewById<TextView>(R.id.txtIngrediente)
        val txtObservaciones = findViewById<TextView>(R.id.txtObservaciones)

        // Obtener datos del Intent
        val noMatricula = intent.getStringExtra("noMatricula")
        val ingredienteId = intent.getStringExtra("ingredienteId")
        val observaciones = intent.getStringExtra("observaciones")

        // Establecer los datos en los TextViews
        txtNoMatricula.text = "Número de Matrícula: $noMatricula"
        txtIngrediente.text = "Ingrediente: $ingredienteId"
        txtObservaciones.text = "Observaciones: $observaciones"
    }
}