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

class AdministradorLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador_login)

        val loginButton = findViewById<Button>(R.id.btnLoginAdmin)
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
        val user = findViewById<EditText>(R.id.etUserAdmin).text.toString()
        val password = findViewById<EditText>(R.id.etPasswordAdmin).text.toString()
        val rolId = 2

        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/login.php"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("LoginResponse", "Respuesta del servidor: $response") // Registro para depuración
                try {
                    // Procesamos la respuesta como JSONArray
                    val responseArray = org.json.JSONArray(response)

                    // Verificar si hay un error en la respuesta
                    if (responseArray.length() > 0) {
                        val userObject = responseArray.getJSONObject(0)

                        // Verificar si la respuesta contiene error
                        if (userObject.has("error")) {
                            val errorMessage = userObject.getString("error")
                            // Mostrar el mensaje de error (por ejemplo, si debe cambiar la contraseña)
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            // Si no hay error, procesamos los datos del usuario
                            if (userObject.getString("nombreUsuario") == user && userObject.getInt("rolId") == rolId) {
                                if (userObject.getString("contrasena") == password) {
                                    // Verificar si hay un mensaje de que falta 1 mes para cambiar la contraseña
                                    if (userObject.has("mensaje")) {
                                        val mensaje = userObject.getString("mensaje")
                                        Toast.makeText(this, "Bienvenido, Administrador $user \n" + mensaje, Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(this, "Bienvenido, Administrador $user", Toast.LENGTH_SHORT).show()
                                    }
                                    val intent = Intent(this, MainActivity_admin::class.java).apply {
                                        putExtra("proximaPagina", "menuPrincipal")
                                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "El ingreso no es válido", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginError", "Error al procesar la respuesta: ${e.message}")
                    Toast.makeText(this, "Error inesperado al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("LoginError", "Error en la solicitud: ${error.message}") // Registro para depuración
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