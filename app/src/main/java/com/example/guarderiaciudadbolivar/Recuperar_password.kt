package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Recuperar_password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_password)

        val etEmailRecover = findViewById<EditText>(R.id.etEmailRecover)
        val btnSendRecovery = findViewById<Button>(R.id.btnSendRecovery)
        val volverBtn = findViewById<Button>(R.id.btnAtras)

        volverBtn.setOnClickListener {
            startActivity(Intent(this, Login_menu::class.java))
            finish()
        }

        btnSendRecovery.setOnClickListener {
            val email = etEmailRecover.text.toString()

            if (email.isNotEmpty()) {
                // Aquí agregarías la lógica para enviar el correo con el enlace de recuperación de contraseña.
                // Por ejemplo, podrías llamar a una función sendPasswordRecoveryEmail(email)

                Toast.makeText(this, "Instrucciones enviadas a tu correo electrónico", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Login_menu::class.java))
                finish()
            } else {
                Toast.makeText(this, "Por favor, ingresa tu correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
