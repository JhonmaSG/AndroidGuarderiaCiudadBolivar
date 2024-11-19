package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

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
            val intent = Intent(this, RegistroUsuario::class.java)
            startActivity(intent)
        }

        // Botón para restablecer la contraseña
        val tvForgotPassword: TextView = findViewById(R.id.tvForgotPassword)
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, recuperar_password_1::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val user = findViewById<EditText>(R.id.etUserProfesor).text.toString()
        val password = findViewById<EditText>(R.id.etPasswordProfesor).text.toString()
        val rolId = 1

        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/login.php";

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("LoginResponse", "Respuesta del servidor: $response")  // Registro para depuración
                try {
                    // Verificamos si la respuesta es un JSONArray
                    val jsonResponse = JSONArray(response)

                    if (jsonResponse.length() > 0) {
                        val userObject = jsonResponse.getJSONObject(0)  // Obtenemos el primer objeto del array

                        // Verificamos si el nombre de usuario y el rol coinciden
                        if (userObject.getString("nombreUsuario") == user && userObject.getInt("rolId") == rolId) {
                            // Usuario autenticado correctamente
                            findViewById<Button>(R.id.btnLoginProfesor).isClickable = false
                            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("proximaPagina", "menuPrincipal")
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        }
                    } else {
                        // Si la respuesta es un array vacío, significa que el usuario no existe
                        Toast.makeText(this, "El usuario no existe o el rol no es válido", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("LoginError", "Error al procesar la respuesta: ${e.message}")
                    Toast.makeText(this, "Error inesperado al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("LoginError", "Error en la solicitud: ${error.message}")  // Registro para depuración
                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                return mapOf(
                    "nombreUsuario" to user,
                    "contrasena" to password,
                    "rolId" to rolId.toString()
                )
            }
        }
        // Crear la cola de solicitudes y añadir la solicitud
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}
