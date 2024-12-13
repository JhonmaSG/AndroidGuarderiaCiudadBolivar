package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class AgregarUsuarios : AppCompatActivity() {

    private lateinit var edtnombre: TextView
    private lateinit var edtcontrasena: TextView
    private lateinit var spinnerRol: Spinner
    private lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_usuarios)

        // Inicializar vistas
        edtnombre = findViewById(R.id.edtnombre)
        edtcontrasena = findViewById(R.id.edtcontrasena)
        spinnerRol = findViewById(R.id.spinnerRol)
        btnIngresar = findViewById(R.id.btnIngresar)

        // Configurar el botón
        btnIngresar.setOnClickListener {
            agregar()
        }
    }

    private fun agregar() {
        val nombre = edtnombre.text.toString().trim()
        val contrasena = edtcontrasena.text.toString().trim()
        val rolSeleccionado = spinnerRol.selectedItem.toString()
        val rol = when (rolSeleccionado) {
            "Administrador" -> "2"
            "Profesor" -> "1"
            else -> ""
        }
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Cargando...")
        }

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Digite el nombre", Toast.LENGTH_SHORT).show()
            return
        }else if (contrasena.isEmpty()) {
            Toast.makeText(this, "Digite la contraseña", Toast.LENGTH_SHORT).show()
            return
        }else if (rol.isEmpty()) {
            Toast.makeText(this, "Seleccione el rol", Toast.LENGTH_SHORT).show()
            return
        }else {
            progressDialog.show()
            val urlGlobal = getString(R.string.url)
            val url2 = "$urlGlobal/insertar_usuarios.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url2,
                { response ->
                    progressDialog.dismiss()
                    if (response.equals("Usuario Registrado", ignoreCase = true)) {
                        Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
                        // Redirigir a la actividad principal (o el fragmento)
                        val intent = Intent(this, MainActivity_admin::class.java)
                        startActivity(intent)
                        finish() // Finaliza la actividad actual para evitar que el usuario vuelva a ella al presionar "Atrás"
                    } else {
                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    progressDialog.dismiss()
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["nombre"] = nombre
                    params["contrasena"] = contrasena
                    params["rol"] = rol
                    return params
                }
            }

            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }
    }
}
