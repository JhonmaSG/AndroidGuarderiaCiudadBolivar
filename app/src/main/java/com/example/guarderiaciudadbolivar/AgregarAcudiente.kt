package com.example.guarderiaciudadbolivar

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AgregarAcudiente : AppCompatActivity() {

    private lateinit var edtCedula: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtDireccion: EditText
    private lateinit var edtParentesco: EditText
    private lateinit var edtNumeroCuenta: EditText
    private lateinit var btnGuardar: Button
    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_acudiente)

        // Inicializar vistas
        edtCedula = findViewById(R.id.edtCedula)
        edtNombre = findViewById(R.id.edtNombre)
        edtDireccion = findViewById(R.id.edtDireccion)
        edtParentesco = findViewById(R.id.edtParentesco)
        edtNumeroCuenta = findViewById(R.id.edtNumeroCuenta)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Configurar ProgressDialog
        progressDialog = AlertDialog.Builder(this)
            .setView(layoutInflater.inflate(R.layout.progress_dialog, null))
            .setCancelable(false)
            .create()

        btnGuardar.setOnClickListener {
            guardarAcudiente()
        }
    }

    private fun guardarAcudiente() {
        if (!validarCampos()) return

        progressDialog.show()

        val urlCompleta = "${getString(R.string.url)}/insertar_acudiente.php"
        val request = object : StringRequest(
            Method.POST,
            urlCompleta,
            { response ->
                progressDialog.dismiss()
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    Toast.makeText(this, "Acudiente guardado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${jsonObject.getString("message")}", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                return hashMapOf(
                    "cedula" to edtCedula.text.toString().trim(),
                    "nombre" to edtNombre.text.toString().trim(),
                    "direccion" to edtDireccion.text.toString().trim(),
                    "parentesco" to edtParentesco.text.toString().trim(),
                    "numeroCuenta" to edtNumeroCuenta.text.toString().trim()
                )
            }
        }

        request.retryPolicy = DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        Volley.newRequestQueue(this).add(request)
    }

    private fun validarCampos(): Boolean {
        val campos = listOf(
            edtCedula to "La cédula es requerida",
            edtNombre to "El nombre es requerido",
            edtDireccion to "La dirección es requerida",
            edtParentesco to "El parentesco es requerido",
            edtNumeroCuenta to "El número de cuenta es requerido"
        )

        for ((campo, mensaje) in campos) {
            if (campo.text.toString().trim().isEmpty()) {
                campo.error = mensaje
                campo.requestFocus()
                return false
            }
        }
        return true
    }
}