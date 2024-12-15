package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class PlatoFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: AdapterPlato
    private val ArrayPlatoList = ArrayList<Plato>()

    companion object {
        fun newInstance(menuId: String): PlatoFragment {
            val fragment = PlatoFragment()
            val bundle = Bundle()
            bundle.putString("menuId", menuId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_platos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.listPlatos)
        adapter = AdapterPlato(requireContext(), ArrayPlatoList)
        listView.adapter = adapter

        val menuId = arguments?.getString("menuId")
        if (menuId != null) {
            cargarPlatos(menuId)
        }
    }

    private fun cargarPlatos(menuId: String) {
        val url = getString(R.string.url) + "/ver_platos.php?menuId=$menuId"
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Cargando platos...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            progressDialog.dismiss()
            Log.d("PlatoFragment", "Respuesta del servidor: $response")
            try {
                ArrayPlatoList.clear()
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val platoObj = jsonArray.getJSONObject(i)
                    val platoId = platoObj.getString("platoId")
                    val nombrePlato = platoObj.getString("nombrePlato")
                    ArrayPlatoList.add(Plato(platoId, nombrePlato))
                }
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("PlatoFragment", "Error al procesar los datos: ${e.message}")
            }
        }, { error ->
            progressDialog.dismiss()
            Log.e("PlatoFragment", "Error de red: ${error.message}")
            Toast.makeText(requireContext(), "Error al cargar los platos", Toast.LENGTH_SHORT).show()
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }
}
