package com.example.guarderiaciudadbolivar



import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarNinoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_nino)

        val edtNoMatricula: EditText = findViewById(R.id.edtNoMatricula)
        val edtNombre: EditText = findViewById(R.id.edtNombre)
        val edtFechaNacimiento: EditText = findViewById(R.id.edtFechaNacimiento)
        val edtFechaIngreso: EditText = findViewById(R.id.edtFechaIngreso)
        val edtFechaFin: EditText = findViewById(R.id.edtFechaFin)
        val edtEstado: EditText = findViewById(R.id.edtEstado)
        val btnActualizar: Button = findViewById(R.id.btnActualizar)

        btnActualizar.setOnClickListener {
            editarNino(
                edtNoMatricula.text.toString(),
                edtNombre.text.toString(),
                edtFechaNacimiento.text.toString(),
                edtFechaIngreso.text.toString(),
                edtFechaFin.text.toString(),
                edtEstado.text.toString()
            )
        }
    }

    private fun editarNino(noMatricula: String, nombre: String, fechaNacimiento: String, fechaIngreso: String, fechaFin: String, estado: String) {
        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/editar_ninos.php"
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
