package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class recuperar_password_3 : AppCompatActivity() {

    private lateinit var etContrasena1: EditText
    private lateinit var etContrasena2: EditText
    private lateinit var btnVerificar: Button
    private lateinit var btnAtras: Button

    private var nombreUsuario: String? = null
    private var dni: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_password3)

        // Inicializando los componentes
        etContrasena1 = findViewById(R.id.etContrasena1)
        etContrasena2 = findViewById(R.id.etContrasena2)
        btnVerificar = findViewById(R.id.btnVerificar)
        btnAtras = findViewById(R.id.btnAtras)

        // Obtener el nombre de usuario y DNI desde el intent o SharedPreferences
        nombreUsuario = intent.getStringExtra("nombreUsuario")
        dni = intent.getStringExtra("dni")

        //comprobación
        if (nombreUsuario != null && dni != null) {
            Log.d("RecuperarContraseña", "Datos recibidos: nombreUsuario = $nombreUsuario, dni = $dni")
        } else {
            Log.e("RecuperarContraseña", "No se recibieron los datos correctamente")
        }

        // Acción de regresar a la pantalla anterior
        btnAtras.setOnClickListener {
            startActivity(Intent(this, recuperar_password_1::class.java))
            finish()
        }

        // Acción para verificar las contraseñas y cambiarlas
        btnVerificar.setOnClickListener {
            val contrasena1 = etContrasena1.text.toString()
            val contrasena2 = etContrasena2.text.toString()

            // Validar que las contraseñas no estén vacías
            if (contrasena1.isEmpty() || contrasena2.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese ambas contraseñas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar que las contraseñas coincidan
            if (contrasena1 != contrasena2) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enviar la nueva contraseña al servidor
            if (nombreUsuario != null && dni != null) {
                // Ejecutar la tarea asíncrona para cambiar la contraseña
                CambiarContrasenaTask().execute(nombreUsuario, dni, contrasena1)
            } else {
                Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show()
                Log.e("RecuperarContraseña", "Datos del usuario no encontrados: nombreUsuario=$nombreUsuario, dni=$dni")
            }
        }
    }

    // AsyncTask para cambiar la contraseña
    inner class CambiarContrasenaTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            val nombreUsuario = params[0]
            val dni = params[1]
            val nuevaContrasena = params[2]

            try {
                val urlGlobal = getString(R.string.url)
                val url = URL("$urlGlobal/cambiar_contrasena.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val postData = "nombreUsuario=$nombreUsuario&dni=$dni&nuevaContrasena=$nuevaContrasena"
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(postData.toByteArray())
                outputStream.flush()
                outputStream.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    // Regresar la respuesta como String
                    return response.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "Error al cambiar la contraseña"
        }

        override fun onPostExecute(result: String) {
            try {
                // Intentar convertir la respuesta en un objeto JSON
                val jsonResponse = JSONObject(result)
                val status = jsonResponse.getString("status")

                if (status == "success") {
                    Toast.makeText(this@recuperar_password_3, "Contraseña cambiada exitosamente", Toast.LENGTH_LONG).show()

                    // Redirigir al login_menu
                    val intent = Intent(this@recuperar_password_3, Login_menu::class.java)
                    startActivity(intent)
                    finish() // Finalizar la actividad actual para que no pueda volver con el botón de atrás
                } else {
                    val message = jsonResponse.getString("message")
                    Toast.makeText(this@recuperar_password_3, "Error: $message", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Manejo de errores si la respuesta no es un JSON válido
                Toast.makeText(this@recuperar_password_3, "Error al procesar la respuesta", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}