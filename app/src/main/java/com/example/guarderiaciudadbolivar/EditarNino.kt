package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarNino : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etFechaIngreso: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnActualizar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_nino)

        etNombre = findViewById(R.id.etEditNombre)
        etFechaNacimiento = findViewById(R.id.etEditFechaNacimiento)
        etFechaIngreso = findViewById(R.id.etEditFechaIngreso)
        spinnerEstado = findViewById(R.id.spinnerEditEstado)
        btnActualizar = findViewById(R.id.btnActualizar)

        val noMatricula = intent.getIntExtra("noMatricula", 0)

        btnActualizar.setOnClickListener {
            actualizarNino(noMatricula)
        }
    }

    private fun actualizarNino(noMatricula: Int) {
        val url = getString(R.string.url) + "/actualizar_nino.php"
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Actualizando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            if (response.trim().equals("datos actualizados", ignoreCase = true)) {
                Toast.makeText(this, "Niño actualizado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            progressDialog.dismiss()
            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["noMatricula"] = noMatricula.toString()
                params["nombre"] = etNombre.text.toString()
                params["fechaNacimiento"] = etFechaNacimiento.text.toString()
                params["fechaIngreso"] = etFechaIngreso.text.toString()
                params["estado"] = spinnerEstado.selectedItem.toString()
                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }
}
