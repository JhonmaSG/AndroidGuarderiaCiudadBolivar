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

class EliminarAlergiaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_alergia)

        val edtNoMatricula: EditText = findViewById(R.id.edtNoMatricula)
        val edtIngredienteId: EditText = findViewById(R.id.edtIngredienteId)
        val btnEliminar: Button = findViewById(R.id.btnEliminar)

        btnEliminar.setOnClickListener {
            eliminarAlergia(
                edtNoMatricula.text.toString(),
                edtIngredienteId.text.toString()
            )
        }
    }

    private fun eliminarAlergia(noMatricula: String, ingredienteId: String) {
        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/eliminar_alergia.php"
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
                    "ingredienteId" to ingredienteId
                )
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}
