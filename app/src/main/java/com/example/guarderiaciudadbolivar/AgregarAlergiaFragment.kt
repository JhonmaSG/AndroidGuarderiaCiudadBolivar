package com.example.guarderiaciudadbolivar

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AgregarAlergiaFragment : Fragment() {

    private lateinit var edtNoMatricula: EditText
    private lateinit var edtIngredienteId: EditText
    private lateinit var edtObservaciones: EditText
    private lateinit var btnAgregarAlergia: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_alergia, container, false)

        edtNoMatricula = view.findViewById(R.id.edtNoMatricula)
        edtIngredienteId = view.findViewById(R.id.edtIngredienteId)
        edtObservaciones = view.findViewById(R.id.edtObservaciones)
        btnAgregarAlergia = view.findViewById(R.id.btnAgregarAlergia)

        btnAgregarAlergia.setOnClickListener {
            agregarAlergia()
        }

        return view
    }

    private fun agregarAlergia() {
        val noMatricula = edtNoMatricula.text.toString().trim()
        val ingredienteId = edtIngredienteId.text.toString().trim()
        val observaciones = edtObservaciones.text.toString().trim()

        if (TextUtils.isEmpty(noMatricula) || TextUtils.isEmpty(ingredienteId) || TextUtils.isEmpty(observaciones)) {
            Toast.makeText(context, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://insertar_alergia.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                if (response.equals("Alergia registrada", ignoreCase = true)) {
                    // Redirigir al fragmento de lista de alergias
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.list_item_alergias, AlergiasFragment())?.commit()
                }
            },
            { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = mutableMapOf<String, String>()
                params["noMatricula"] = noMatricula
                params["ingredienteId"] = ingredienteId
                params["observaciones"] = observaciones
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}
