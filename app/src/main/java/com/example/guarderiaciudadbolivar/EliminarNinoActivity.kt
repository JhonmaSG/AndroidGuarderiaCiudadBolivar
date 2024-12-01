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

class EliminarNinoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_nino)

        val edtNoMatricula: EditText = findViewById(R.id.edtNoMatricula)
        val btnEliminar: Button = findViewById(R.id.btnEliminar)

        btnEliminar.setOnClickListener {
            eliminarNino(edtNoMatricula.text.toString())
        }
    }

    private fun eliminarNino(noMatricula: String) {
        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/eliminar_ninos.php"
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return hashMapOf(
                    "noMatricula" to noMatricula
                )
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}
