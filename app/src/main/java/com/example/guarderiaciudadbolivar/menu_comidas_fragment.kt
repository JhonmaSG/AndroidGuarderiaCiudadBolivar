package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuAdapter
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class menu_comidas_fragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: AdapterMenuComidas
    private val ArrayMenuList = ArrayList<MenuComidas>()
    fun getMenuList(): List<MenuComidas> {
        return ArrayMenuList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout correspondiente al fragmento
        return inflater.inflate(R.layout.fragment_menu_comidas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            // Inicializar la lista y el adaptador
            listView = view.findViewById(R.id.listMenuComida)
            adapter = AdapterMenuComidas(requireContext(), ArrayMenuList)
            listView.adapter = adapter

            // Cargar los menús desde el servidor
            cargarMenus()
        } catch (e: Exception) {
            Log.e("MenuComidasFragment", "Error al inicializar el fragmento: ${e.message}")
        }
    }

    private fun cargarMenus() {
        val url = getString(R.string.url) + "/ver_menu_platos.php"
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val stringRequest = StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            Log.d("MenuComidasFragment", "Respuesta del servidor: $response")  // Añadir esto para ver la respuesta real
            try {
                ArrayMenuList.clear()
                // El response es directamente un JSONArray
                val jsonArray = JSONArray(response)

                for (i in 0 until jsonArray.length()) {
                    val menuObj = jsonArray.getJSONObject(i)
                    val menuId = menuObj.getString("noMenu")  // Cambié "menuId" por "noMenu" para que coincida con tu respuesta
                    val nombreMenu = menuObj.getString("nombreMenu")
                    ArrayMenuList.add(MenuComidas(menuId, nombreMenu))
                }
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("MenuComidasFragment", "Error al procesar los datos: ${e.message}")
            }
        }, { error ->
            progressDialog.dismiss()
            Log.e("MenuComidasFragment", "Error de red: ${error.message}")
            Toast.makeText(requireContext(), "Error al cargar los menús", Toast.LENGTH_SHORT).show()
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }
}