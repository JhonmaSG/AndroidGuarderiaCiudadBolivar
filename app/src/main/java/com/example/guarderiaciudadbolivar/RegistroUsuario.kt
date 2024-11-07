package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class RegistroUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        val etNombreUsuario = findViewById<EditText>(R.id.etNombreCuenta)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val etDni = findViewById<EditText>(R.id.etDni)
        val etNombres = findViewById<EditText>(R.id.etNombre)
        val etApellidos = findViewById<EditText>(R.id.etApellido)
        val etRespuesta = findViewById<EditText>(R.id.etRespuesta)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val regresoButton = findViewById<Button>(R.id.btnAtras)

        val spinnerPre = findViewById<Spinner>(R.id.spinnerPregunta)
        val spinnerRol = findViewById<Spinner>(R.id.spinnerRol)
        val opcionesPre = arrayOf(
            "Seleccione una Pregunta de Seguridad",
            "Mascota favorita",
            "Lugar favorito",
            "Comida favorita",
            "Película favorita",
            "Cosa favorita"
        )
        val opcionesRol = arrayOf(
            "Seleccione su Rol",
            "Profesor",
            "Administrativo"
        )

        val adapterPre = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesPre)
        adapterPre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPre.adapter = adapterPre

        val adapterRol = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesRol)
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRol.adapter = adapterRol


        regresoButton.setOnClickListener {
            startActivity(Intent(this, ProfesorLogin::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            val nombreUsuario = etNombreUsuario.text.toString()
            val contrasena = etContrasena.text.toString()
            val rol = spinnerRol.selectedItem.toString()
            val dni = etDni.text.toString()
            val nombre = etNombres.text.toString()
            val apellido = etApellidos.text.toString()
            val pregunta = spinnerPre.selectedItem.toString()
            val respuesta = etRespuesta.text.toString()

            if (nombreUsuario.isNotEmpty() && contrasena.isNotEmpty() && dni.isNotEmpty() &&
                rol != "Seleccione su Rol" && nombre.isNotEmpty() && apellido.isNotEmpty() &&
                pregunta != "Seleccione una Pregunta de Seguridad" && respuesta.isNotEmpty()) {
                // Enviar datos al servidor con AsyncTask
                RegisterUserTask().execute(nombreUsuario, contrasena, rol, dni, nombre, apellido, pregunta, respuesta)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente", Toast.LENGTH_LONG).show()
            }
        }
    }

    inner class RegisterUserTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            val nombreUsuario = params[0]
            val contrasena = params[1]
            val rol = params[2]
            val dni = params[3]
            val nombre = params[4]
            val apellido = params[5]
            val pregunta = params[6]
            val respuesta = params[7]

            try {
                // Establecer la conexión
                val url = URL("http://192.168.0.6/guarderia/registrar_usuario.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                // Crear los parámetros del POST
                val postData = "nombreUsuario=$nombreUsuario&contrasena=$contrasena&rol=$rol&dni=$dni&nombre=$nombre&apellido=$apellido&pregunta=$pregunta&respuesta=$respuesta"

                // Enviar los datos
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(postData.toByteArray())
                outputStream.flush()
                outputStream.close()

                // Leer la respuesta del servidor
                val responseCode = connection.responseCode
                return if (responseCode == HttpURLConnection.HTTP_OK) {
                    "Registro exitoso"
                } else {
                    "Error: $responseCode"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error de conexión"
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()

            // Verificar si el registro fue exitoso y redirigir al login_menu
            if (result == "Registro exitoso") {
                // Redirigir a la actividad de login (login_menu)
                val intent = Intent(this@RegistroUsuario, Login_menu::class.java)
                startActivity(intent)
                finish()  // Finalizar la actividad actual y evita devolverse
            }
        }
    }
}