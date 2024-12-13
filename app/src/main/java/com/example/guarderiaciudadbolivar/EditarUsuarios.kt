package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarUsuarios : AppCompatActivity() {

    private lateinit var edtid: EditText
    private lateinit var edtnombre: EditText
    private lateinit var edtcontrasena: EditText
    private lateinit var spinnerRol: Spinner
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuarios)

        // Inicializar vistas
        edtid = findViewById(R.id.edtid)
        edtnombre = findViewById(R.id.edtnombre)
        edtcontrasena = findViewById(R.id.edtcontrasena)
        spinnerRol = findViewById(R.id.spinnerRol)

        // Configurar el Spinner con los roles
        val rolesArray = resources.getStringArray(R.array.roles_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rolesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRol.adapter = adapter

        // Obtener datos del intent
        val usuarioId = intent.getStringExtra("usuarioId")
        val nombreUsuario = intent.getStringExtra("nombreUsuario")
        val contrasena = intent.getStringExtra("contrasena")
        val rolId = intent.getStringExtra("rolId")

        if (usuarioId != null && nombreUsuario != null && contrasena != null && rolId != null) {
            edtid.setText(usuarioId)
            edtnombre.setText(nombreUsuario)
            edtcontrasena.setText(contrasena)

            // Establecer el rol seleccionado en el Spinner
            val rolesArray = resources.getStringArray(R.array.roles_array)
            val rolPosition = when (rolId) {
                "2" -> 0 // Profesor
                "1" -> 1 // Administrador
                else -> -1 // Default o manejo de error
            }

            if (rolPosition >= 0) {
                spinnerRol.setSelection(rolPosition)
            }
        } else {
            Toast.makeText(this, "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    fun actualizar(view: View) {
        val id = edtid.text.toString().trim()
        val nombre = edtnombre.text.toString().trim()
        val contrasena = edtcontrasena.text.toString().trim()
        // Obtener el rol seleccionado en el Spinner
        val rolSeleccionado = spinnerRol.selectedItem.toString()

        if (id.isEmpty() || nombre.isEmpty() || contrasena.isEmpty() || rolSeleccionado.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Imprime los valores en Logcat
        Log.d("EditarUsuarios", "ID enviado: $id, Nombre enviado: $nombre, Contraseña enviada: $contrasena, Rol enviado: $rolSeleccionado")

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Actualizando...")
        progressDialog.show()

        val urlGlobal = getString(R.string.url)
        val urlEditarUsuarios = "$urlGlobal/editar_usuarios.php"
        Log.d("EditarUsuarios", "URL utilizada: $urlEditarUsuarios")
        val request = object : StringRequest(Request.Method.POST, urlEditarUsuarios, { response ->
            progressDialog.dismiss()
            Log.d("EditarUsuarios", "Respuesta del servidor: $response")
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            // Regresar al fragmento después de la actualización
            startActivity(Intent(this, MainActivity_admin::class.java))
            finish()
        }, { error ->
            progressDialog.dismiss()
            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            Log.e("EditarUsuarios", "Error: ${error.message}")
            if (error.networkResponse != null) {
                val statusCode = error.networkResponse.statusCode
                val responseBody = String(error.networkResponse.data, Charsets.UTF_8)
                Log.e("EditarUsuarios", "Error - StatusCode: $statusCode, Body: $responseBody")
            }
        }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("usuarioId", id)
                    put("nombreUsuario", nombre)
                    put("contrasena", contrasena)
                    val rolId = when (rolSeleccionado) {
                        "Profesor" -> "1"
                        "Administrador" -> "2"
                        else -> "0"  // Default o manejo de error
                    }
                    put("rolId", rolId)
                }
            }
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}
