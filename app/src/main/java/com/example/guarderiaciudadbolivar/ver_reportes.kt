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

    private var noMatricula = "";
    private var nomNino = ""

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
            Log.d("ver_reportes", "El botón fue presionado, llamando a generarPDF()")
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

                        // Dividir la información
                        nomNino = data.getString("nombreNino")
                        noMatricula = data.getString("noMatricula")

                        datosNino = """
                        Nombre del Niño: ${data.getString("nombreNino")}
                        Matrícula: ${data.getString("noMatricula")}
                        Fecha de Nacimiento: ${data.getString("fechaNacimiento")}
                        Fecha de Ingreso: ${data.getString("fechaIngreso")}
                        Fecha de Fin: ${data.getString("fechaFin")}
                        Estado: ${data.getString("estadoNino")}
                    """.trimIndent()

                        datosAcudiente = """
                        Nombre del Acudiente: ${data.getString("nombreAcudiente")}
                        Dirección: ${data.getString("direccion")}
                        Parentesco: ${data.getString("parentesco")}
                        Teléfono: ${data.getString("telefono")}
                    """.trimIndent()

                        datosAlergias = """
                        Alergias: ${data.getString("alergiasObservaciones")}
                        Ingrediente Alergénico: ${data.getString("alergiaIngrediente")}
                    """.trimIndent()

                        // Mostrar los datos en el TextView
                        tvDatosReporte.text = "Datos del Niño: \n$datosNino\n\nDatos del Acudiente: \n$datosAcudiente\n\nAlergias: \n$datosAlergias"

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

            // Función para dibujar texto con salto de línea
            fun drawTextWithLineBreaks(text: String, startX: Float, startY: Float, canvas: Canvas, paint: Paint): Float {
                val lines = text.split("\n") // Dividir el texto en líneas
                var yPosition = startY
                for (line in lines) {
                    canvas.drawText(line, startX, yPosition, paint)
                    yPosition += 20f // Espacio entre líneas
                }
                return yPosition // Retorna la nueva posición Y después de escribir todas las líneas
            }

            // Escribir el título
            var yPosition = 20f
            var xPosition = 120f
            canvas.drawText("REPORTE", xPosition, yPosition, paint)
            yPosition += 40f // Espacio después del título

            // Escribir los datos del niño
            canvas.drawText("Datos del Niño", xPosition-10, yPosition, paint)
            yPosition += 20f

            // Llamar a la función para imprimir los datos del niño con saltos de línea
            yPosition = drawTextWithLineBreaks(datosNino, 20f, yPosition, canvas, paint)
            yPosition += 40f  // Espacio entre secciones

            // Escribir los datos del acudiente
            canvas.drawText("Datos del Acudiente", xPosition-20, yPosition, paint)
            yPosition += 20f

            // Llamar a la función para imprimir los datos del acudiente
            yPosition = drawTextWithLineBreaks(datosAcudiente, 20f, yPosition, canvas, paint)
            yPosition += 40f  // Espacio entre secciones

            // Escribir los datos de alergias
            canvas.drawText("Alergias", xPosition, yPosition, paint)
            yPosition += 20f

            // Llamar a la función para imprimir los datos de alergias
            yPosition = drawTextWithLineBreaks(datosAlergias, 20f, yPosition, canvas, paint)

            // Terminar la página
            pdfDocument.finishPage(page)

            // Obtener el directorio de "Descargas" en el almacenamiento externo
            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val nombreArchivo = "$noMatricula-$nomNino.pdf"

            val filePath = File(downloadsDirectory, "$nombreArchivo.pdf")

            // Guardar el PDF en el almacenamiento externo
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
