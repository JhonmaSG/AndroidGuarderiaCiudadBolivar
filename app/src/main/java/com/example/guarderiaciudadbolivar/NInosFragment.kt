package com.example.guarderiaciudadbolivar

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

class NinosFragment : Fragment() {

    private lateinit var listViewNinos: ListView
    private lateinit var adapter: AdapterNinos
    private val listaNinos = mutableListOf<Nino>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ninos, container, false)

        listViewNinos = view.findViewById(R.id.listViewNinos)
        cargarNinos()

        listViewNinos.setOnItemClickListener { _, _, position, _ ->
            val nino = listaNinos[position]
            val fragment = DetallesNinoFragment.newInstance(nino.noMatricula)
            requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun cargarNinos() {
        val url = getString(R.string.url) + "/obtener_ninos.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonArray = JSONArray(response)
                listaNinos.clear()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val nino = Nino(
                        obj.getInt("noMatricula"),
                        obj.getString("nombre"),
                        obj.getString("fechaNacimiento"),
                        obj.getString("fechaIngreso"),
                        obj.optString("fechaFin"),
                        obj.getString("estado")
                    )
                    listaNinos.add(nino)
                }
                adapter = AdapterNinos(requireContext(), listaNinos)
                listViewNinos.adapter = adapter
            },
            { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }
}
