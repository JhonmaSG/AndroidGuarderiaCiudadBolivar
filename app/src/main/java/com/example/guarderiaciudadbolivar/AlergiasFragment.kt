package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class AlergiasFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: AdapterAlergias
    private val alergiasList: MutableList<Alergias> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alergias, container, false)
        listView = view.findViewById(R.id.listViewAlergias)
        obtenerAlergias()
        return view
    }

    private fun obtenerAlergias() {
        val url = "http://obtener_alergias.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val alergia = jsonArray.getJSONObject(i)
                    val noMatricula = alergia.getInt("noMatricula")
                    val ingredienteId = alergia.getInt("ingredienteId")
                    val observaciones = alergia.getString("observaciones")
                    alergiasList.add(Alergias(noMatricula, ingredienteId, observaciones))
                }
                adapter = AdapterAlergias(requireContext(), alergiasList)
                listView.adapter = adapter
            },
            { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        )
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}
