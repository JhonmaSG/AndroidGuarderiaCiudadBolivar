package com.example.guarderiaciudadbolivar

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AgregarNinoActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripcion_children)


        val edtNoMatricula: EditText = findViewById(R.id.edtNoMatricula)
        val edtNombre: EditText = findViewById(R.id.edtNombre)
        val edtFechaNacimiento: EditText = findViewById(R.id.edtFechaNacimiento)
        val edtFechaIngreso: EditText = findViewById(R.id.edtFechaIngreso)
        val edtFechaFin: EditText = findViewById(R.id.edtFechaFin)
        val edtEstado: EditText = findViewById(R.id.edtEstado)
        val btnGuardar: Button = findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            agregarNino(
                edtNoMatricula.text.toString(),
                edtNombre.text.toString(),
                edtFechaNacimiento.text.toString(),
                edtFechaIngreso.text.toString(),
                edtFechaFin.text.toString(),
                edtEstado.text.toString()
            )
        }
    }

    private fun agregarNino(noMatricula: String, nombre: String, fechaNacimiento: String, fechaIngreso: String, fechaFin: String, estado: String) {
        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/insertar_ninos.php"
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return hashMapOf(
                    "noMatricula" to noMatricula,
                    "nombre" to nombre,
                    "fechaNacimiento" to fechaNacimiento,
                    "fechaIngreso" to fechaIngreso,
                    "fechaFin" to fechaFin,
                    "estado" to estado
                )
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}
