package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.kernel.pdf.PdfDocument
import org.json.JSONArray
import org.json.JSONException
import java.io.File
import java.io.FileOutputStream

class ver_reportes_admin : Fragment(R.layout.fragment_reportes_admin) {

    private lateinit var spinnerTipoReporte: Spinner
    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaFin: EditText
    private lateinit var btnGenerarReporte: Button
    private lateinit var tvResultadoReporte: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerTipoReporte = view.findViewById(R.id.spinner_tipo_reporte)
        etFechaInicio = view.findViewById(R.id.etFechaInicio)
        etFechaFin = view.findViewById(R.id.etFechaFin)
        btnGenerarReporte = view.findViewById(R.id.btn_generar_reporte)
        tvResultadoReporte = view.findViewById(R.id.tv_resultado_reporte)

        btnGenerarReporte.setOnClickListener {
            val tipoReporte = spinnerTipoReporte.selectedItem.toString()
            val fechaInicio = etFechaInicio.text.toString()
            val fechaFin = etFechaFin.text.toString()

            // Verificar si las fechas están vacías
            if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                Log.e("VerReportesAdminFragment", "Fechas vacías, por favor ingrese las fechas.")
                Toast.makeText(requireContext(), "Por favor, ingrese las fechas.", Toast.LENGTH_LONG).show()
            } else {
                Log.d("VerReportesAdminFragment", "Fechas seleccionadas: $fechaInicio - $fechaFin")
                when (tipoReporte) {
                    "Reporte de Pagos" -> {
                        Log.d("VerReportesAdminFragment", "Generando reporte de pagos...")
                        solicitarReporte("reporte_pagos_admin.php", fechaInicio, fechaFin, tipoReporte)
                    }
                    "Reporte de Consumos" -> {
                        Log.d("VerReportesAdminFragment", "Generando reporte de consumos...")
                        solicitarReporte("reporte_consumos_admin.php", fechaInicio, fechaFin, tipoReporte)
                    }
                    "Reporte de Alergias" -> {
                        Log.d("VerReportesAdminFragment", "Generando reporte de alergias...")
                        solicitarReporte("reporte_alergias_admin.php", fechaInicio, fechaFin, tipoReporte)
                    }
                    else -> {
                        Log.e("VerReportesAdminFragment", "Tipo de reporte no válido.")
                        Toast.makeText(requireContext(), "Selecciona un reporte válido.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun solicitarReporte(endpoint: String, fechaInicio: String, fechaFin: String, tipoReporte: String) {
        val url = getString(R.string.url) + "/" + endpoint + "?fechaInicio=$fechaInicio&fechaFin=$fechaFin"

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // Verifica si la respuesta es JSON
                    Log.d("Response", response.toString())
                    if (response.getString("status") == "success") {
                        val data = response.getJSONArray("data")
                        if (checkPermissions()) {
                            generarPDF(data, tipoReporte)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error en el reporte.", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    Log.e("VerReportesAdminFragment", "Error en el parsing JSON: ${e.message}")
                    Toast.makeText(requireContext(), "Error al procesar la respuesta.", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("VerReportesAdminFragment", "Error: ${error.message}")
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    private fun generarPDF(data: JSONArray, tipoReporte: String) {
        try {
            // Definir la ruta y el archivo en la carpeta de descargas
            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            // Cambiar el nombre del archivo según el tipo de reporte
            val fileName = "${tipoReporte.replace(" ", "_")}.pdf" // Reemplaza espacios por guiones bajos

            val filePath = File(downloadsDirectory, fileName)

            // Verifica si el directorio existe, si no, lo crea
            if (!downloadsDirectory.exists()) {
                downloadsDirectory.mkdirs()
            }

            // Crear el escritor de PDF
            val writer = PdfWriter(FileOutputStream(filePath))

            // Crear el documento PDF
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)

            // Título del reporte
            document.add(Paragraph("$tipoReporte"))

            // Agregar los datos al PDF
            for (i in 0 until data.length()) {
                val row = data.getJSONObject(i)
                val line = buildString {
                    // Controlar el contenido de acuerdo al tipo de reporte
                    when (tipoReporte) {
                        "Reporte de Consumos" -> {
                            // Reporte de Consumos: usar fechaConsumo y CostoPorComida
                            append("Fecha de Consumo: ${row.getString("fechaConsumo")}, ")
                            append("Costo por comida: ${row.getDouble("CostoPorComida")}, ")
                        }
                        "Reporte de Pagos" -> {
                            // Reporte de Pagos: usar fechaPago y valorPago
                            append("Fecha de Pago: ${row.getString("fechaPago")}, ")
                            append("Valor de Pago: ${row.getDouble("valorPago")}, ")
                        }
                        "Reporte de Alergias" -> {
                            // Reporte de Alergias: incluir fecha_alergia y alergiaIngrediente
                            append("Fecha Consumo: ${row.getString("FechaConsumo")}, ")
                            append("Observaciones: ${row.getString("Observaciones")}, ")
                            append("Menú: ${row.getString("NombreMenu")}, ")
                            append("Plato: ${row.getString("NombrePlato")}, ")
                            append("Niño: ${row.getString("NombreNiño")}")
                        }
                        else -> {
                            // Si el tipo de reporte no es reconocido, mostrar un mensaje de error
                            append("Tipo de reporte no válido. ")
                        }
                    }
                }
                document.add(Paragraph(line))
            }

            // Cerrar el documento
            document.close()

            Toast.makeText(requireContext(), "Reporte generado en Descargas", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Log.e("VerReportesAdminFragment", "Error al generar el PDF: ${e.message}")
            Toast.makeText(requireContext(), "Error al generar el PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    // Método para verificar permisos (implementación sencilla)
    private fun checkPermissions(): Boolean {
        // Aquí puedes verificar los permisos necesarios para la escritura en almacenamiento
        // Deberías solicitar permisos de almacenamiento si es necesario
        return true
    }
}
