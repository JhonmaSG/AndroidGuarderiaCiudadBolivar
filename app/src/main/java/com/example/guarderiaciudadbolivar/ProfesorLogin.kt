package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfesorLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profesor_login)

        val loginButton = findViewById<Button>(R.id.btnLoginProfesor)
        loginButton.setOnClickListener { loginUser() }

        val regresarButton = findViewById<Button>(R.id.btnAtras)

        regresarButton.setOnClickListener {
            startActivity(Intent(this, Login_menu::class.java))
            finish()
        }

        // Botón de registro
        val tvRegister: TextView = findViewById(R.id.tvRegistrar)
        tvRegister.setOnClickListener {
            val intent = Intent(this, Registro_profesor::class.java)
            startActivity(intent)
        }

        // Botón para restablecer la contraseña
        val tvForgotPassword: TextView = findViewById(R.id.tvForgotPassword)
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, Recuperar_password::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.etEmailProfesor).text.toString()
        val password = findViewById<EditText>(R.id.etPasswordProfesor).text.toString()

        // Lógica para autenticar contra tu base de datos
        if (validateCredentials(email, password, "profesor")) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCredentials(email: String, password: String, role: String): Boolean {
        // Aquí se implementa la lógica de conexión a tu base de datos
        // Deberías comprobar si el usuario existe y si la contraseña coincide.
        // Este método debería devolver `true` si las credenciales son válidas.
        return true // Modifica esta línea con la lógica real
    }
}