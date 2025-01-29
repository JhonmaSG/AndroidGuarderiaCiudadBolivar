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
import org.json.JSONArray
import org.json.JSONObject

class EditarAlergias : AppCompatActivity() {
    private lateinit var spinnerNinos: Spinner
    private lateinit var spinnerIngredientes: Spinner
    private lateinit var edtObservaciones: EditText

    private val ninosList = ArrayList<String>()
    private val ingredientesList = ArrayList<String>()

    private var noMatriculaOriginal: String = ""
    private var ingredienteIdOriginal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_alergias)

        spinnerNinos = findViewById(R.id.spinnerNinos)
        spinnerIngredientes = findViewById(R.id.spinnerIngredientes)
        edtObservaciones = findViewById(R.id.edtObservaciones)

        // Obtener datos del intent
        noMatriculaOriginal = intent.getStringExtra("noMatricula") ?: ""
        ingredienteIdOriginal = intent.getStringExtra("ingredienteId") ?: ""
        val observaciones = intent.getStringExtra("observaciones") ?: ""

        cargarNinos()
        cargarIngredientes()

        edtObservaciones.setText(observaciones)
    }

    private fun cargarNinos() {
        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/ver_ninos.php"

        val stringRequest = StringRequest(Request.Method.POST, url, { response ->
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")

                if (success == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val noMatricula = obj.getString("noMatricula")
                        ninosList.add(noMatricula)
                    }

                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ninosList)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerNinos.adapter = adapter

                    // Establecer la selección original
                    val positionNino = ninosList.indexOf(noMatriculaOriginal)
                    if (positionNino != -1) {
                        spinnerNinos.setSelection(positionNino)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error al cargar niños: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(this, "Error de red: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun cargarIngredientes() {
        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/ver_ingredientes.php"

        val stringRequest = StringRequest(Request.Method.POST, url, { response ->
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")

                if (success == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val nombreIngrediente = obj.getString("nombreIngrediente")
                        ingredientesList.add(nombreIngrediente)
                    }

                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ingredientesList)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerIngredientes.adapter = adapter

                    // Encontrar y establecer el ingrediente original
                    val positionIngrediente = ingredientesList.indexOf(ingredienteIdOriginal)
                    if (positionIngrediente != -1) {
                        spinnerIngredientes.setSelection(positionIngrediente)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error al cargar ingredientes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(this, "Error de red: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    fun actualizar(view: View) {
        val noMatricula = spinnerNinos.selectedItem.toString()
        val nombreIngrediente = spinnerIngredientes.selectedItem.toString()
        val observaciones = edtObservaciones.text.toString().trim()

        if (observaciones.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Actualizando...")
        progressDialog.show()

        val urlGlobal = getString(R.string.url)
        val url3 = "$urlGlobal/editar_alergias.php"

        val request = object : StringRequest(Request.Method.POST, url3, { response ->
            progressDialog.dismiss()
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity_admin::class.java))
            finish()
        }, { error ->
            progressDialog.dismiss()
            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("noMatriculaOriginal", noMatriculaOriginal)
                    put("ingredienteIdOriginal", ingredienteIdOriginal)
                    put("noMatricula", noMatricula)
                    put("nombreIngrediente", nombreIngrediente)
                    put("observaciones", observaciones)
                }
            }
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}