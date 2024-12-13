package com.example.guarderiaciudadbolivar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AgregarNinoFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var etAlergias: EditText
    private lateinit var btnAgregar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_agregar_nino, container, false)

        etNombre = view.findViewById(R.id.etNombre)
        etEdad = view.findViewById(R.id.etEstado)
        etFechaNacimiento = view.findViewById(R.id.etFechaNacimiento)
        btnAgregar = view.findViewById(R.id.btnAgregar)

        btnAgregar.setOnClickListener {
            agregarNino()
        }

        return view
    }

    private fun agregarNino() {
        val nombre = etNombre.text.toString()
        val edad = etEdad.text.toString().toIntOrNull()
        val alergias = etAlergias.text.toString()

        if (nombre.isNotEmpty() && edad != null) {
            val url = "https://tu-servidor.com/crear_nino.php"
            val params = mapOf("nombre" to nombre, "edad" to edad.toString(), "alergias" to alergias)

            val request = object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                    //findNavController().navigateUp() // Regresa al listado
                },
                Response.ErrorListener { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): Map<String, String> = params
            }

            Volley.newRequestQueue(requireContext()).add(request)
        } else {
            Toast.makeText(requireContext(), "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }
}

