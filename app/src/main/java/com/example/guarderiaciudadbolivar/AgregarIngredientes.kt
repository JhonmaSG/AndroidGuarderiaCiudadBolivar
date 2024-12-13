package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class AgregarIngredientes : AppCompatActivity() {

    private lateinit var edtnombre: TextView
    private lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_ingredientes)

        // Inicializar vistas
        edtnombre = findViewById(R.id.edtnombre)
        btnIngresar = findViewById(R.id.btnIngresar)

        // Configurar el botón
        btnIngresar.setOnClickListener {
            agregar()
        }
    }

    private fun agregar() {
        val nombre = edtnombre.text.toString().trim()
        val progressDialog = ProgressDialog(this).apply {
            setMessage("Cargando...")
        }

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Digite el nombre", Toast.LENGTH_SHORT).show()
            return
        } else {
            progressDialog.show()
            val urlGlobal = getString(R.string.url)
            val url2 = "$urlGlobal/insertar_ingredientes.php"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url2,
                { response ->
                    progressDialog.dismiss()
                    if (response.equals("Ingrediente Registrado", ignoreCase = true)) {
                        Toast.makeText(this, "Ingrediente Registrado", Toast.LENGTH_SHORT).show()
                        // Redirigir a la actividad principal (o el fragmento)
                        val intent = Intent(this, menu_comidas_fragment::class.java)
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
                    return params
                }
            }

            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }
    }
}
