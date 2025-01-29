package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class AgregarAlergias : AppCompatActivity() {
    private lateinit var spinnerNinos: Spinner
    private lateinit var spinnerIngredientes: Spinner
    private lateinit var edtObservaciones: EditText
    private lateinit var btnIngresar: Button

    private val ninosList = ArrayList<String>()
    private val ingredientesList = ArrayList<String>()
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_alergias)

        spinnerNinos = findViewById(R.id.spinnerNinos)
        spinnerIngredientes = findViewById(R.id.spinnerIngredientes)
        edtObservaciones = findViewById(R.id.edtObservaciones)
        btnIngresar = findViewById(R.id.btnIngresar)

        requestQueue = Volley.newRequestQueue(this)

        cargarNinos()
        cargarIngredientes()

        btnIngresar.setOnClickListener {
            agregar()
        }
    }

    private fun cargarNinos() {
        val url = "${getString(R.string.url)}/ver_ninos.php"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("success") == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    ninosList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        ninosList.add(obj.getString("noMatricula"))
                    }
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ninosList)
                    spinnerNinos.adapter = adapter
                } else {
                    Toast.makeText(this, "No hay niños disponibles", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(this, "Error de conexión: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        requestQueue.add(stringRequest)
    }

    private fun cargarIngredientes() {
        val url = "${getString(R.string.url)}/ver_ingredientes.php"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("success") == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    ingredientesList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        ingredientesList.add(obj.getString("nombreIngrediente"))
                    }
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ingredientesList)
                    spinnerIngredientes.adapter = adapter
                } else {
                    Toast.makeText(this, "No hay ingredientes disponibles", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(this, "Error de conexión: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        requestQueue.add(stringRequest)
    }

    private fun agregar() {
        val noMatricula = spinnerNinos.selectedItem?.toString() ?: ""
        val nombreIngrediente = spinnerIngredientes.selectedItem?.toString() ?: ""
        val observaciones = edtObservaciones.text.toString().trim()

        if (noMatricula.isEmpty() || nombreIngrediente.isEmpty() || observaciones.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this).apply {
            setMessage("Registrando alergia...")
            show()
        }

        val url = "${getString(R.string.url)}/insertar_alergias.php"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            { response ->
                progressDialog.dismiss()
                if (response.trim().equals("Alergia Registrada", ignoreCase = true)) {
                    Toast.makeText(this, "Alergia registrada con éxito", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity_admin::class.java))
                    finish()
                } else {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return hashMapOf(
                    "noMatricula" to noMatricula,
                    "nombreIngrediente" to nombreIngrediente,
                    "observaciones" to observaciones
                )
            }
        }

        requestQueue.add(stringRequest)
    }
}
