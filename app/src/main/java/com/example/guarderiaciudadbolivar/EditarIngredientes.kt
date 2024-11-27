package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarIngredientes : AppCompatActivity() {

    private lateinit var edtid: EditText
    private lateinit var edtnombre: EditText
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_ingredientes)

        // Inicializar vistas
        edtid = findViewById(R.id.edtid)
        edtnombre = findViewById(R.id.edtnombre)

        // Obtener datos del intent
        val ingredienteId = intent.getStringExtra("ingredienteId")
        val nombreIngrediente = intent.getStringExtra("nombreIngrediente")

        if (ingredienteId != null && nombreIngrediente != null) {
            edtid.setText(ingredienteId)
            edtnombre.setText(nombreIngrediente)
        } else {
            Toast.makeText(this, "Error al cargar los datos del ingrediente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    fun actualizar(view: View) {
        val id = edtid.text.toString().trim()
        val nombre = edtnombre.text.toString().trim()

        if (id.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Imprime los valores en Logcat
        Log.d("EditarIngredientes", "ID enviado: $id, Nombre enviado: $nombre")

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Actualizando...")
        progressDialog.show()

        val urlGlobal = getString(R.string.url)
        val url3 = "$urlGlobal/editar_ingredientes.php"
        val request = object : StringRequest(Request.Method.POST, url3, { response ->
            progressDialog.dismiss()
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            // Regresar al fragmento después de la actualización
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, { error ->
            progressDialog.dismiss()
            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("ingredienteId", id)
                    put("nombreIngrediente", nombre)
                }
            }
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}
