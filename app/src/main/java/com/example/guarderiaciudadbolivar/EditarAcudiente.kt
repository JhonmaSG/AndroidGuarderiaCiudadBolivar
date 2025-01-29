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

class EditarAcudiente : AppCompatActivity() {

    private lateinit var edtCedula: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtDireccion: EditText
    private lateinit var edtParentesco: EditText
    private lateinit var edtNumeroCuenta: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_acudiente)

        // Inicializar vistas
        edtCedula = findViewById(R.id.edtCedula)
        edtNombre = findViewById(R.id.edtNombre)
        edtDireccion = findViewById(R.id.edtDireccion)
        edtParentesco = findViewById(R.id.edtParentesco)
        edtNumeroCuenta = findViewById(R.id.edtNumeroCuenta)

        // Obtener datos del intent
        val cedula = intent.getStringExtra("cedula")
        val nombre = intent.getStringExtra("nombre")
        val direccion = intent.getStringExtra("direccion")
        val parentesco = intent.getStringExtra("parentesco")
        val numeroCuenta = intent.getStringExtra("numeroCuenta")

        if (cedula != null && nombre != null && direccion != null && parentesco != null && numeroCuenta != null) {
            edtCedula.setText(cedula)
            edtNombre.setText(nombre)
            edtDireccion.setText(direccion)
            edtParentesco.setText(parentesco)
            edtNumeroCuenta.setText(numeroCuenta)
        } else {
            Toast.makeText(this, "Error al cargar los datos del acudiente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun actualizar(view: View) {
        val cedula = edtCedula.text.toString().trim()
        val nombre = edtNombre.text.toString().trim()
        val direccion = edtDireccion.text.toString().trim()
        val parentesco = edtParentesco.text.toString().trim()
        val numeroCuenta = edtNumeroCuenta.text.toString().trim()

        if (cedula.isEmpty() || nombre.isEmpty() || direccion.isEmpty() || parentesco.isEmpty() || numeroCuenta.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("EditarAcudiente", "Cédula enviada: $cedula, Nombre: $nombre")

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Actualizando...")
        progressDialog.show()

        val urlGlobal = getString(R.string.url)
        val url = "$urlGlobal/actualizar_acudiente.php"
        val request = object : StringRequest(Request.Method.POST, url, { response ->
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
                return hashMapOf(
                    "cedula" to cedula,
                    "nombre" to nombre,
                    "direccion" to direccion,
                    "parentesco" to parentesco,
                    "numeroCuenta" to numeroCuenta
                )
            }
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}
