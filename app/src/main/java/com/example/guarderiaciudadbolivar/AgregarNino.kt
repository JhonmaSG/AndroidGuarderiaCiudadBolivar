package com.example.guarderiaciudadbolivar

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.text.SimpleDateFormat
import java.util.*

class AgregarNino : AppCompatActivity() {
    private lateinit var etNoMatricula: EditText
    private lateinit var etNombre: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etFechaIngreso: EditText
    private lateinit var etCedulaAcudiente: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nino)

        // Inicialización de vistas
        etNoMatricula = findViewById(R.id.etNoMatricula)
        etNombre = findViewById(R.id.etNombre)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etFechaIngreso = findViewById(R.id.etFechaIngreso)
        etCedulaAcudiente = findViewById(R.id.etCedulaAcudiente)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Cargar estados en el Spinner
        cargarEstados()

        // Configurar inputs de fecha
        etFechaNacimiento.isFocusable = false
        etFechaIngreso.isFocusable = false

        etFechaNacimiento.setOnClickListener { showDatePickerDialog(etFechaNacimiento) }
        etFechaIngreso.setOnClickListener { showDatePickerDialog(etFechaIngreso) }

        // Configurar botón Guardar
        btnGuardar.setOnClickListener { guardarNino() }
    }

    private fun cargarEstados() {
        val estados = arrayOf("Seleccionar...", "Activo", "Inactivo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun guardarNino() {
        val noMatricula = etNoMatricula.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val fechaNacimiento = etFechaNacimiento.text.toString().trim()
        val fechaIngreso = etFechaIngreso.text.toString().trim()
        val acudienteCedula = etCedulaAcudiente.text.toString().trim()
        val estado = spinnerEstado.selectedItem.toString()

        // Validación de campos
        if (noMatricula.isEmpty() || nombre.isEmpty() || fechaNacimiento.isEmpty() ||
            fechaIngreso.isEmpty() || acudienteCedula.isEmpty() || estado == "Seleccionar...") {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación adicional para asegurar que noMatricula y acudienteCedula sean números
        try {
            noMatricula.toInt()
            acudienteCedula.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "La matrícula y cédula deben ser números", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostrar ProgressDialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Guardando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val url = "${getString(R.string.url)}/agregar_nino.php"

        Log.d("GuardarNino", "URL: $url")
        Log.d("GuardarNino", "Datos a enviar: noMatricula=$noMatricula, nombre=$nombre, " +
                "fechaNacimiento=$fechaNacimiento, fechaIngreso=$fechaIngreso, " +
                "acudienteCedula=$acudienteCedula, estado=$estado")

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                Log.d("GuardarNino", "Respuesta del servidor: $response")
                progressDialog.dismiss()
                if (response.equals("datos guardados", ignoreCase = true)) {
                    Toast.makeText(this, "Niño Guardado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("GuardarNino", "Error: ${error.message}", error)
                progressDialog.dismiss()
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    put("noMatricula", noMatricula)
                    put("nombre", nombre)
                    put("fechaNacimiento", fechaNacimiento)
                    put("fechaIngreso", fechaIngreso)
                    put("acudienteCedula", acudienteCedula)
                    put("estado", estado)
                }
            }
        }

        // Configurar timeout más largo
        stringRequest.retryPolicy = com.android.volley.DefaultRetryPolicy(
            30000, // 30 segundos de timeout
            0, // no reintentos
            1f
        )

        Volley.newRequestQueue(this).add(stringRequest)
    }
}