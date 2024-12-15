package com.example.guarderiaciudadbolivar

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import android.os.Environment
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ver_reportes : Fragment() {
    // Declarar las variables a nivel de clase
    private lateinit var tvDatosReporte: TextView
    private lateinit var btnGenerarPDF: Button

    private lateinit var datosReporte: String

    private var datosNino = ""
    private var datosAcudiente = ""
    private var datosAlergias = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño del fragmento
        return inflater.inflate(R.layout.fragment_ver_reportes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los componentes del layout
        val etNumeroMatricula: EditText = view.findViewById(R.id.etNumeroMatricula)
        val etNombreEstudiante: EditText = view.findViewById(R.id.etNombreEstudiante)
        val btnGenerarReporte: Button = view.findViewById(R.id.btnGenerarReporte)

        // Inicializar las variables tvDatosReporte y btnGenerarPDF
        tvDatosReporte = view.findViewById(R.id.tvDatosReporte)
        btnGenerarPDF = view.findViewById(R.id.btnGenerarPDF)

        // Configurar el botón para generar el reporte
        btnGenerarReporte.setOnClickListener {
            val numeroMatricula = etNumeroMatricula.text.toString()
            val nombreEstudiante = etNombreEstudiante.text.toString()

            if (numeroMatricula.isEmpty() && nombreEstudiante.isEmpty()) {
                Toast.makeText(context, "Por favor, ingrese al menos un dato", Toast.LENGTH_SHORT).show()
            } else {
                // Lógica para generar el reporte
                obtenerDatosNino(numeroMatricula, nombreEstudiante)
            }
        }

        btnGenerarPDF.setOnClickListener {
            generarPDF()
        }
    }

    // Cambiar el método obtenerDatosNino para que no pase tvDatosReporte ni btnGenerarPDF como parámetros
    private fun obtenerDatosNino(numeroMatricula: String, nombreEstudiante: String) {
        val url = getString(R.string.url) + "/reporte_nino.php"
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                Log.d("ayayaya_reportes", "Respuesta del servidor: $response")
                try {
                    val jsonResponse = JSONObject(response)
                    if (jsonResponse.getBoolean("success")) {
                        val data = jsonResponse.getJSONArray("data").getJSONObject(0)

                        // Mostrar los datos en el TextView
                        datosReporte = """
                        Nombre del Niño: ${data.getString("nombreNino")}
                        Matrícula: ${data.getString("noMatricula")}
                        Fecha de Nacimiento: ${data.getString("fechaNacimiento")}
                        Fecha de Ingreso: ${data.getString("fechaIngreso")}
                        Fecha de Fin: ${data.getString("fechaFin")}
                        Estado: ${data.getString("estadoNino")}
                        Nombre del Acudiente: ${data.getString("nombreAcudiente")}
                        Dirección: ${data.getString("direccion")}
                        Parentesco: ${data.getString("parentesco")}
                        Número de Cuenta: ${data.getString("numeroCuenta")}
                        Teléfono: ${data.getString("telefono")}
                        Alergias: ${data.getString("alergiasObservaciones")}
                        Ingrediente Alergénico: ${data.getString("alergiaIngrediente")}
                    """.trimIndent()
                        tvDatosReporte.text = datosReporte

                        // Hacer visible el botón para generar el PDF
                        btnGenerarPDF.visibility = View.VISIBLE

                    } else {
                        tvDatosReporte.text = "No se encontraron datos para la matrícula o el nombre proporcionado."
                        Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error al procesar los datos", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                Log.d("Datos Enviados", "Matricula: $numeroMatricula, Nombre: $nombreEstudiante")
                val params = HashMap<String, String>()
                if (numeroMatricula.isNotEmpty()) {
                    params["numeroMatricula"] = numeroMatricula
                }
                if (nombreEstudiante.isNotEmpty()) {
                    params["nombreEstudiante"] = nombreEstudiante
                }
                return params
            }
        }

        // Añadir la solicitud a la cola
        Volley.newRequestQueue(context).add(stringRequest)
    }

    // Método para generar el PDF
    private fun generarPDF() {
        try {
            // Crear un documento PDF
            val pdfDocument = android.graphics.pdf.PdfDocument()
            val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(300, 600, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            // Crear un objeto Canvas para dibujar en la página
            val canvas: Canvas = page.canvas
            val paint = Paint()
            paint.color = Color.BLACK
            paint.textSize = 12f
            paint.isAntiAlias = true

            // Escribir los datos en el PDF
            var yPosition = 20f
            canvas.drawText("Reporte de Niño", 20f, yPosition, paint)
            yPosition += 20f

            // Usar los datosReporte almacenados
            canvas.drawText(datosReporte, 20f, yPosition, paint)

            // Terminar la página
            pdfDocument.finishPage(page)

            // Guardar el PDF en el almacenamiento interno
            val filePath = File(Environment.getExternalStorageDirectory(), "reporte_nino.pdf")
            pdfDocument.writeTo(FileOutputStream(filePath))

            // Mostrar un mensaje indicando que el PDF se generó con éxito
            Toast.makeText(context, "PDF generado con éxito", Toast.LENGTH_SHORT).show()

            // Cerrar el documento
            pdfDocument.close()
        } catch (e: IOException) {
            Log.e("ver_reportes", "Error al generar el PDF: ${e.message}")
            Toast.makeText(context, "Error al generar el PDF", Toast.LENGTH_SHORT).show()
        }
    }

}
