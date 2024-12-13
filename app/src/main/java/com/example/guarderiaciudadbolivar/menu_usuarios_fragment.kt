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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject

class menu_usuarios_fragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: AdapterUsuarios
    private val usuariosArrayList = ArrayList<Usuarios>()
    fun getUsuariosList(): List<Usuarios> {
        return usuariosArrayList
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout correspondiente al fragmento
        return inflater.inflate(R.layout.fragment_menu_usuarios, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            // Configurar el clic del botón flotante
            val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButtonUsuarios)
            floatingButton.setOnClickListener {
                agregarUsuario()
            }
            // Inicializar la lista y el adaptador
            listView = view.findViewById(R.id.listUsuario)
            adapter = AdapterUsuarios(requireContext(), usuariosArrayList)
            listView.adapter = adapter
            // Manejar clics en los elementos de la lista
            listView.setOnItemClickListener { _, _, position, _ ->
                val builder = AlertDialog.Builder(requireContext())
                val dialogItems = arrayOf("Ver Datos", "Editar Usuario", "Eliminar Usuario")
                builder.setTitle(usuariosArrayList[position].nombreUsuario)
                builder.setItems(dialogItems) { _, i ->
                    when (i) {
                        0 -> {
                            val intent = Intent(requireContext(), DetallesUsuarios::class.java)
                            intent.putExtra("usuarioId", usuariosArrayList[position].usuarioId)
                            intent.putExtra("nombreUsuario", usuariosArrayList[position].nombreUsuario)
                            intent.putExtra("contrasena", usuariosArrayList[position].contrasena)
                            intent.putExtra("rolId", usuariosArrayList[position].rolId)
                            Log.d("menu_usuarios_fragment", "Usuario ID: ${usuariosArrayList[position].usuarioId}")
                            Log.d("menu_usuarios_fragment", "Nombre Usuario: ${usuariosArrayList[position].nombreUsuario}")
                            Log.d("menu_usuarios_fragment", "Contraseña: ${usuariosArrayList[position].contrasena}")
                            Log.d("menu_usuarios_fragment", "Rol: ${usuariosArrayList[position].rolId}")
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(requireContext(), EditarUsuarios::class.java)
                            intent.putExtra(
                                "usuarioId",
                                usuariosArrayList[position].usuarioId
                            )
                            intent.putExtra(
                                "nombreUsuario",
                                usuariosArrayList[position].nombreUsuario
                            )
                            intent.putExtra(
                                "contrasena",
                                usuariosArrayList[position].contrasena
                            )
                            intent.putExtra(
                                "rolId",
                                usuariosArrayList[position].rolId
                            )
                            startActivity(intent)
                        }
                        2 -> eliminarUsuario(usuariosArrayList[position].usuarioId)
                    }
                }
                builder.create().show()
            }
            // Cargar los ingredientes desde el servidor
            cargarUsuarios()
        } catch (e: Exception) {
            Log.e("menu_usuarios_fragment", "Error al inicializar el fragmento: ${e.message}")
        }
    }
    private fun eliminarUsuario(id: String) {
        val urlGlobal = getString(R.string.url)
        val urlEliminar = "$urlGlobal/eliminar_usuarios.php"
        // Agregar log para verificar el id que se está enviando
        Log.d("eliminarUsuario", "ID enviado: $id")
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Eliminando...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val stringRequest = object : StringRequest(Request.Method.POST, urlEliminar, { response ->
            progressDialog.dismiss()
            // Eliminar espacios en blanco o saltos de línea antes de comparar
            val responseTrimmed = response.trim()
            // Agregar log para verificar la respuesta recibida del servidor
            Log.d("eliminarUsuario", "Respuesta del servidor (recortada): $responseTrimmed")
            if (responseTrimmed.equals("datos eliminados", ignoreCase = true)) {
                Toast.makeText(requireContext(), "Usuario Eliminado", Toast.LENGTH_SHORT).show()
                cargarUsuarios()
            } else {
                Toast.makeText(requireContext(), "No se pudo eliminar el Usuario", Toast.LENGTH_SHORT).show()
                cargarUsuarios()
            }
        }, { error ->
            progressDialog.dismiss()
            // Agregar log en caso de error en la solicitud
            Log.e("eliminarUsuario", "Error en la solicitud: ${error.message}")
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
    private fun cargarUsuarios() {
        val url = getString(R.string.url) + "/ver_usuarios.php"
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Cargando...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val stringRequest = StringRequest(Request.Method.POST, url, { response ->
            progressDialog.dismiss()
            try {
                usuariosArrayList.clear()
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                if (success == "1") {
                    val jsonArray: JSONArray = jsonObject.getJSONArray("datos")
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getString("usuarioId")
                        val nombre = obj.getString("nombreUsuario")
                        val contrasena = obj.getString("contrasena")
                        val rol = obj.getString("rolId")
                        val usuario = Usuarios(id, nombre, contrasena, rol)
                        usuariosArrayList.add(usuario)
                    }
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("menu_usuarios_fragment", "Error al procesar los datos: ${e.message}")
            }
        }, { error ->
            progressDialog.dismiss()
            Log.e("menu_usuarios_fragment", "Error de red: ${error.message}")
        })
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }
    fun agregarUsuario() {
        startActivity(Intent(requireContext(), AgregarUsuarios::class.java))
    }
}