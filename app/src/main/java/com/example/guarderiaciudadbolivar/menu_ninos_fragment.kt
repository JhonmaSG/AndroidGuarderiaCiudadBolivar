package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException

class menu_ninos_fragment : Fragment() {

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
                .replace(R.id.fragment_conteiner_usuario, fragment)
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
                if (response.startsWith("[") || response.startsWith("{")) {
                    try {
                        val jsonArray = JSONArray(response)
                        listaNinos.clear()
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val nino = Nino(
                                obj.getInt("noMatricula"),
                                obj.getString("nombre"),
                                obj.getString("fechaNacimiento"),
                                obj.getString("fechaIngreso"),
                                obj.optString("fechaFin", ""),
                                obj.getString("estado")
                            )
                            listaNinos.add(nino)
                        }
                        adapter = AdapterNinos(requireContext(), listaNinos)
                        listViewNinos.adapter = adapter
                    } catch (e: JSONException) {
                        Log.e("JSONError", "Error al procesar los datos: ${e.message}")
                        Toast.makeText(
                            requireContext(),
                            "Error al procesar los datos del servidor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("JSONError", "Respuesta no válida: $response")
                    Toast.makeText(
                        requireContext(),
                        "Respuesta no válida del servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            { error ->
                Log.e("VolleyError", "Error: ${error.message}")
                Toast.makeText(
                    requireContext(),
                    "Error de red: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }
}