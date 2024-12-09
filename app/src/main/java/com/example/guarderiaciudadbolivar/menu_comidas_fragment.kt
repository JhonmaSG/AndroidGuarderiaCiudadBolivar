package com.example.guarderiaciudadbolivar

<<<<<<< HEAD
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class menu_comidas_fragment : Fragment() {
    // Variables privadas para almacenar parámetros pasados al fragment
    private var param1: String? = null
    private var param2: String? = null

    // Método llamado al crear el fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Recuperar los argumentos pasados al fragment
            arguments?.let {
                // Obtener param1 y param2 de los argumentos
                param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
            }
        } catch (e: Exception) {
            // Manejo de excepciones durante la recuperación de argumentos
            Log.e("menu_comidas_fragment", "Error al recuperar argumentos: ${e.message}")
        }
    }

    // Método para inflar la vista del fragment
=======
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

>>>>>>> origin/dev
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
<<<<<<< HEAD
        // Inflar el layout correspondiente al fragment
        return inflater.inflate(R.layout.fragment_menu_comidas, container, false)
    }

    // Método que se llama después de que la vista ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aquí puedes manejar la lógica de interacción con los elementos de la vista
        // Por ejemplo, configurar un botón o una lista con los datos recibidos
        try {
            // Ejemplo de interacción: mostrar los parámetros en un TextView
            //val textView: TextView = view.findViewById(R.id.textView)
            //textView.text = "Nombre: $param1, Dirección: $param2"
        } catch (e: Exception) {
            // Manejo de excepciones al acceder a elementos de la vista
            Log.e("menu_comidas_fragment", "Error al establecer el texto: ${e.message}")
        }
    }

    // Compañero de objeto para crear nuevas instancias del fragment
    companion object {
        // Método estático para crear una nueva instancia del fragment con parámetros
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            menu_comidas_fragment().apply {
                arguments = Bundle().apply {
                    // Almacenar los parámetros en un Bundle
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
=======
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
>>>>>>> origin/dev
    }
}
