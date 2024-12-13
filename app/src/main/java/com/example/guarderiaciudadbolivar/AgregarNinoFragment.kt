package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AgregarNinoFragment : Fragment() {

    private lateinit var edtNombre: EditText
    private lateinit var edtFechaNacimiento: EditText
    private lateinit var edtFechaIngreso: EditText
    private lateinit var btnGuardar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_nino, container, false)

        edtNombre = view.findViewById(R.id.edtNombre)
        edtFechaNacimiento = view.findViewById(R.id.edtFechaNacimiento)
        edtFechaIngreso = view.findViewById(R.id.edtFechaIngreso)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener { guardarNino() }

        return view
    }

    private fun guardarNino() {
        val nombre = edtNombre.text.toString().trim()
        val fechaNacimiento = edtFechaNacimiento.text.toString().trim()
        val fechaIngreso = edtFechaIngreso.text.toString().trim()

        if (nombre.isEmpty() || fechaNacimiento.isEmpty() || fechaIngreso.isEmpty()) {
            Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val url = getString(R.string.url) + "/insertar_nino.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                if (response.equals("Niño Registrado", true)) {
                    Toast.makeText(requireContext(), "Niño registrado exitosamente", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nombre"] = nombre
                params["fechaNacimiento"] = fechaNacimiento
                params["fechaIngreso"] = fechaIngreso
                return params
            }
        }
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }
}
