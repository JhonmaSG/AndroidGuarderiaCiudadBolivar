package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallesUsuarios : AppCompatActivity() {

    private lateinit var txtId: TextView
    private lateinit var txtNombre: TextView
    private lateinit var txtContrasena: TextView
    private lateinit var txtRol: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_usuarios)

        // Vincular vistas con sus IDs
        txtId = findViewById(R.id.txtid)
        txtNombre = findViewById(R.id.txtnombre)
        txtContrasena = findViewById(R.id.txtcontrasena)
        txtRol = findViewById(R.id.txtrol)

        // Obtener datos desde el Intent
        val usuarioId = intent.getStringExtra("usuarioId")
        val nombreUsuario = intent.getStringExtra("nombreUsuario")
        val contrasena = intent.getStringExtra("contrasena")
        val rolId = intent.getStringExtra("rolId")

        val rolTexto = getRolTexto(rolId)

        // Log para ver qué datos están llegando
        Log.d("DetallesIngredientes", "Usuario ID: $usuarioId")
        Log.d("DetallesIngredientes", "Nombre Usuario: $nombreUsuario")
        Log.d("DetallesIngredientes", "Contraseña: $contrasena")
        Log.d("DetallesIngredientes", "Rol: $rolId")

        // Mostrar los detalles del ingrediente si están disponibles
        if (usuarioId != null && nombreUsuario != null && contrasena != null && rolId != null) {
            txtId.text = "ID: $usuarioId"
            txtNombre.text = "Nombre: $nombreUsuario"
            txtContrasena.text = "Contraseña: $contrasena"
            txtRol.text = "Rol: $rolTexto"
        } else {
            txtId.text = "ID no disponible"
            txtNombre.text = "Nombre no disponible"
            txtContrasena.text = "Contraseña no disponible"
            txtRol.text = "Rol no disponible"
        }

    }

    private fun getRolTexto(rolId: String?): String {
        return when (rolId) {
            "1" -> "Profesor"
            "2" -> "Administrador"
            else -> "Rol desconocido"
        }
    }
}

