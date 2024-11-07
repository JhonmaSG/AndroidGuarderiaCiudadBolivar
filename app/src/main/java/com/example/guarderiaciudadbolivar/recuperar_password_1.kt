package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

class recuperar_password_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_password1)

        val etNombreUsuario = findViewById<EditText>(R.id.etNombreCuenta)
        val etDni = findViewById<EditText>(R.id.etDni)
        val btnVerificar = findViewById<Button>(R.id.btnVerificar)
        val volverBtn = findViewById<Button>(R.id.btnAtras)

        volverBtn.setOnClickListener {
            startActivity(Intent(this, Login_menu::class.java))
            finish()
        }

        btnVerificar.setOnClickListener {
            val nomUsuario = etNombreUsuario.text.toString().trim()
            val dni = etDni.text.toString().trim()

            if (nomUsuario.isNotEmpty() && dni.isNotEmpty()) {
                // Ejecutar la tarea para verificar el usuario en la base de datos
                VerificarUsuarioTask().execute(nomUsuario, dni)
            } else {
                Toast.makeText(this, "Por favor, ingrese todos los datos", Toast.LENGTH_LONG).show()
            }
        }
    }

    inner class VerificarUsuarioTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val nombreUsuario = params[0]
            val dni = params[1]

            return try {
                val url = URL("http://192.168.0.6/guarderia/verificar_usuario.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val postData = "nombreUsuario=$nombreUsuario&dni=$dni"
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(postData.toByteArray())
                outputStream.flush()
                outputStream.close()

                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                inputStream.forEachLine { response.append(it) }
                inputStream.close()

                response.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        override fun onPostExecute(result: String) {
            if (result.isNotEmpty()) {
                try {
                    val jsonResponse = JSONObject(result)
                    val status = jsonResponse.getString("status")

                    if (status == "success") {
                        // Si la verificación es exitosa, redirige a recuperar_password_2 y pasa los datos
                        val intent = Intent(this@recuperar_password_1, recuperar_password_2::class.java)
                        // Pasar los valores de nombreUsuario y dni a la siguiente actividad
                        val nombreUsuario = findViewById<EditText>(R.id.etNombreCuenta).text.toString()
                        val dni = findViewById<EditText>(R.id.etDni).text.toString()
                        intent.putExtra("nombreUsuario", nombreUsuario)
                        intent.putExtra("dni", dni)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@recuperar_password_1,
                            jsonResponse.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@recuperar_password_1,
                        "Error de respuesta",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(this@recuperar_password_1, "Error de conexión", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}