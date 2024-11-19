package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class recuperar_password_2 : AppCompatActivity() {

    private lateinit var nombreUsuario: String
    private lateinit var dni: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_password2)

        // Recuperar los valores pasados desde recuperar_password_1.kt
        nombreUsuario = intent.getStringExtra("nombreUsuario").toString()
        dni = intent.getStringExtra("dni").toString()

        Log.d("RecuperarContraseña", "Datos recibidos: nombreUsuario = $nombreUsuario, dni = $dni")

        // Verificar si los valores están vacíos
        if (nombreUsuario.isNullOrEmpty() || dni.isNullOrEmpty()) {
            Log.e("RecuperarContraseña", "Los datos de nombreUsuario o dni están vacíos")
            Toast.makeText(this, "No se recibieron los datos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        // Referencia al TextView donde se mostrará la pregunta
        val txtPregunta = findViewById<TextView>(R.id.txtPregunta)
        val etRespuesta = findViewById<EditText>(R.id.etRespuesta)
        val btnSendRecovery = findViewById<Button>(R.id.btnSendRecovery)
        val btnAtras = findViewById<Button>(R.id.btnAtras)

        // Obtener la pregunta de seguridad del usuario
        ObtenerPreguntaSeguridadTask().execute(nombreUsuario, dni)

        btnSendRecovery.setOnClickListener {
            val respuesta = etRespuesta.text.toString()
            if (respuesta.isNotEmpty()) {
                // Lógica para verificar la respuesta de seguridad
                VerificarRespuestaTask().execute(nombreUsuario, dni, respuesta)
            } else {
                Toast.makeText(
                    this,
                    "Por favor, ingrese la respuesta a la pregunta de seguridad",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        btnAtras.setOnClickListener {
            startActivity(Intent(this, recuperar_password_1::class.java))
            finish()
        }
    }

    // Asynctask para obtener la pregunta de seguridad
    inner class ObtenerPreguntaSeguridadTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            val nombreUsuario = params[0]
            val dni = params[1]

            try {
                val urlGlobal = getString(R.string.url)
                val url = URL("$urlGlobal/obtener_pregunta.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val postData = "nombreUsuario=$nombreUsuario&dni=$dni"
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

                    // Analizar la respuesta JSON
                    val jsonResponse = JSONObject(response.toString())
                    val status = jsonResponse.getString("status")

                    if (status == "success") {
                        return jsonResponse.getString("pregunta")
                    } else {
                        return "Error: ${jsonResponse.getString("message")}"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "Error al obtener la pregunta"
        }

        override fun onPostExecute(result: String) {
            Log.d("RecuperarContraseña", "Respuesta del servidor: $result")
            // Actualizar el TextView con la pregunta recibida
            val txtPregunta = findViewById<TextView>(R.id.txtPregunta)
            if (result.isNotEmpty() && !result.startsWith("Error")) {
                txtPregunta.text = result
            } else {
                txtPregunta.text = "Pregunta de seguridad no disponible"
            }
        }
    }

    // Asynctask para verificar la respuesta de seguridad
    inner class VerificarRespuestaTask : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg params: String?): Boolean {
            val nombreUsuario = params[0]
            val dni = params[1]
            val respuesta = params[2]

            try {
                // URL del script PHP que verificará la respuesta en el servidor
                val url = URL("http://192.168.0.6/guarderia/verificar_respuesta.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                // Parámetros que se envían al servidor
                val postData = "nombreUsuario=$nombreUsuario&dni=$dni&respuesta=$respuesta"

                // Enviar datos al servidor
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(postData.toByteArray())
                outputStream.flush()
                outputStream.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leer la respuesta del servidor
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    // Si la respuesta es "correcta", la respuesta es válida
                    return response.toString() == "correcta"
                } else {
                    // Si la respuesta del servidor no es 200 OK, consideramos que la verificación falló
                    return false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false  // Si ocurre un error, devolver false
        }

        override fun onPostExecute(result: Boolean) {
            if (result) {
                // Si la verificación es exitosa, redirige a recuperar_password_3 y pasa los datos
                val intent = Intent(this@recuperar_password_2, recuperar_password_3::class.java)
                intent.putExtra("nombreUsuario", nombreUsuario)
                intent.putExtra("dni", dni)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@recuperar_password_2, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
