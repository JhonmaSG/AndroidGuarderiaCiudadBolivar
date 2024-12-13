package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallesIngredientes : AppCompatActivity() {

    private lateinit var txtId: TextView
    private lateinit var txtNombre: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_ingredientes)

        // Vincular vistas con sus IDs
        txtId = findViewById(R.id.txtid)
        txtNombre = findViewById(R.id.txtnombre)

        // Obtener datos desde el Intent
        val ingredienteId = intent.getStringExtra("ingredienteId")
        val nombreIngrediente = intent.getStringExtra("nombreIngrediente")

        // Log para ver qué datos están llegando
        Log.d("DetallesIngredientes", "Ingrediente ID: $ingredienteId")
        Log.d("DetallesIngredientes", "Nombre Ingrediente: $nombreIngrediente")

        // Mostrar los detalles del ingrediente si están disponibles
        if (ingredienteId != null && nombreIngrediente != null) {
            txtId.text = "ID: $ingredienteId"
            txtNombre.text = "Nombre: $nombreIngrediente"
        } else {
            txtId.text = "ID no disponible"
            txtNombre.text = "Nombre no disponible"
        }

    }
}

