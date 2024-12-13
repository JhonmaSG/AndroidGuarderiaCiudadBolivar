package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class DetallesNinoFragment : Fragment() {

    private lateinit var txtNombre: TextView
    private lateinit var txtFechaNacimiento: TextView
    private lateinit var txtFechaIngreso: TextView
    private lateinit var txtFechaFin: TextView
    private lateinit var txtEstado: TextView
    private var noMatricula: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalles_nino, container, false)

        txtNombre = view.findViewById(R.id.txtNombre)
        txtFechaNacimiento = view.findViewById(R.id.txtFechaNacimiento)
        txtFechaIngreso = view.findViewById(R.id.txtFechaIngreso)
        txtFechaFin = view.findViewById(R.id.txtFechaFin)
        txtEstado = view.findViewById(R.id.txtEstado)

        // Obtener la matrícula del niño
        noMatricula = arguments?.getInt("noMatricula") ?: 0

        cargarDetallesNino()

        return view
    }

    private fun cargarDetallesNino() {
        val url = getString(R.string.url) + "/obtener_detalles_nino.php?noMatricula=$noMatricula"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Suponiendo que la respuesta es un JSON con los datos del niño
                val obj = JSONObject(response)
                txtNombre.text = obj.getString("nombre")
                txtFechaNacimiento.text = obj.getString("fechaNacimiento")
                txtFechaIngreso.text = obj.getString("fechaIngreso")
                txtFechaFin.text = obj.optString("fechaFin", "N/A")
                txtEstado.text = obj.getString("estado")
            },
            { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }

    companion object {
        fun newInstance(noMatricula: Int): DetallesNinoFragment {
            val fragment = DetallesNinoFragment()
            val args = Bundle()
            args.putInt("noMatricula", noMatricula)
            fragment.arguments = args
            return fragment
        }
    }
}
