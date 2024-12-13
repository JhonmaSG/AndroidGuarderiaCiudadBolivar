package com.example.guarderiaciudadbolivar


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
class menu_comidas_fragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: Adapter
    private val ingredientesArrayList = ArrayList<Ingredientes>()
    fun getIngredientesList(): List<Ingredientes> {
        return ingredientesArrayList
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
            // Configurar el clic del botón flotante
            val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            floatingButton.setOnClickListener {
                agregarIngrediente()
            }
            // Inicializar la lista y el adaptador
            listView = view.findViewById(R.id.listIngrediente)
            adapter = Adapter(requireContext(), ingredientesArrayList)
            listView.adapter = adapter
            // Manejar clics en los elementos de la lista
            listView.setOnItemClickListener { _, _, position, _ ->
                val builder = AlertDialog.Builder(requireContext())
                val dialogItems = arrayOf("Ver Datos", "Editar Ingrediente", "Eliminar Ingrediente")
                builder.setTitle(ingredientesArrayList[position].nombreIngrediente)
                builder.setItems(dialogItems) { _, i ->
                    when (i) {
                        0 -> {
                            val intent = Intent(requireContext(), DetallesIngredientes::class.java)
                            intent.putExtra("ingredienteId", ingredientesArrayList[position].ingredienteId)
                            intent.putExtra("nombreIngrediente", ingredientesArrayList[position].nombreIngrediente)
                            Log.d("menu_comidas_fragment", "Ingrediente ID: ${ingredientesArrayList[position].ingredienteId}")
                            Log.d("menu_comidas_fragment", "Nombre Ingrediente: ${ingredientesArrayList[position].nombreIngrediente}")
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(requireContext(), EditarIngredientes::class.java)
                            intent.putExtra(
                                "ingredienteId",
                                ingredientesArrayList[position].ingredienteId
                            )
                            intent.putExtra(
                                "nombreIngrediente",
                                ingredientesArrayList[position].nombreIngrediente
                            )
                            startActivity(intent)
                        }
                        2 -> eliminarIngrediente(ingredientesArrayList[position].ingredienteId)
                    }
                }
                builder.create().show()
            }
            // Cargar los ingredientes desde el servidor
            cargarIngredientes()
        } catch (e: Exception) {
            Log.e("menu_comidas_fragment", "Error al inicializar el fragmento: ${e.message}")
        }
    }
    private fun eliminarIngrediente(id: String) {
        val urlGlobal = getString(R.string.url)
        val urlEliminar = "$urlGlobal/eliminar_ingredientes.php"
        // Agregar log para verificar el id que se está enviando
        Log.d("eliminarIngrediente", "ID enviado: $id")
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Eliminando...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val stringRequest = object : StringRequest(Request.Method.POST, urlEliminar, { response ->
            progressDialog.dismiss()
            // Eliminar espacios en blanco o saltos de línea antes de comparar
            val responseTrimmed = response.trim()
            // Agregar log para verificar la respuesta recibida del servidor
            Log.d("eliminarIngrediente", "Respuesta del servidor (recortada): $responseTrimmed")
            if (responseTrimmed.equals("datos eliminados", ignoreCase = true)) {
                Toast.makeText(requireContext(), "Ingrediente Eliminado", Toast.LENGTH_SHORT).show()
                cargarIngredientes()
            } else {
                Toast.makeText(requireContext(), "No se pudo eliminar el ingrediente", Toast.LENGTH_SHORT).show()
                cargarIngredientes()
            }
        }, { error ->
            progressDialog.dismiss()
            // Agregar log en caso de error en la solicitud
            Log.e("eliminarIngrediente", "Error en la solicitud: ${error.message}")
            Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id
                return params
            }
        }
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }
    private fun cargarIngredientes() {
        val url = getString(R.string.url) + "/ver_ingredientes.php"
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val stringRequest = StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            try {
                ingredientesArrayList.clear()
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getString("ingredienteId")
                        val nombre = obj.getString("nombreIngrediente")
                        val ingrediente = Ingredientes(id, nombre)
                        ingredientesArrayList.add(ingrediente)
                    }
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("menu_comidas_fragment", "Error al procesar los datos: ${e.message}")
            }
        }, { error ->
            progressDialog.dismiss()
            Log.e("menu_comidas_fragment", "Error de red: ${error.message}")
        })
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }
    fun agregarIngrediente() {
        startActivity(Intent(requireContext(), AgregarIngredientes::class.java))
    }
}
