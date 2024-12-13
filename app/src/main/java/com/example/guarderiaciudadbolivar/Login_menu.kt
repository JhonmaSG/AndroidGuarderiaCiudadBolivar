package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Login_menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_menu)

        val profesorButton = findViewById<Button>(R.id.btnProfesor)
        val administradorButton = findViewById<Button>(R.id.btnAdministrador)

        profesorButton.setOnClickListener {
            // Redirige al login para profesor
            val intent = Intent(this, ProfesorLogin::class.java)
            startActivity(intent)
        }

        administradorButton.setOnClickListener {
            // Redirige al login para administrador
            val intent = Intent(this, AdministradorLogin::class.java)
            startActivity(intent)
        }
    }
}