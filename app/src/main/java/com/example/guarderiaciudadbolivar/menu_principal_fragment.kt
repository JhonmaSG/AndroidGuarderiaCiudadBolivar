package com.example.guarderiaciudadbolivar

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.CookieHandler
import java.net.CookieManager

class menu_principal_fragment : Fragment() {
    // Variables privadas para almacenar parámetros pasados al fragment (opcionales)
    private var name: String? = null
    private var address: String? = null

    // Método llamado al crear el fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {

            /***
            // Recuperar los argumentos pasados al fragment
            arguments?.let {
                // Obtener el nombre y la dirección de los argumentos
                name = it.getString(NAME)
                address = it.getString(ADDRESS)
                // Registrar el nombre en el log para depuración
                Log.i("aris", name.orEmpty())
            }**/
        } catch (e: Exception) {
            // Manejo de excepciones durante la recuperación de argumentos
            Log.e("menu_principal_fragment", "Error al recuperar argumentos: ${e.message}")
        }
    }

    // Método para inflar la vista del fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_principal, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    // Lógica adicional del Fragment puede ser implementada aquí
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val cerrar_sesion_usuario: ImageView? = view?.findViewById(R.id.cerrar_sesion_user)
            obtenerUsuario()
            cerrar_sesion_usuario?.let {
                cerrar_sesion_usuario.setImageResource(R.drawable.boton_de_encendido)
                cerrar_sesion_usuario.setOnClickListener {
                    showOpcionesDesplegable(requireContext())
                }

            }
        } catch (e: Exception) {
            // Manejo de excepciones para la lógica del view
            Log.e("menu_principal_fragment", "Error en onViewCreated: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showOpcionesDesplegable(context: Context) {
        try {
            // Crear un PopupMenu utilizando el contexto y el estilo personalizado
            val popupMenu = PopupMenu(ContextThemeWrapper(context, R.style.CustomPopupMenu), view?.findViewById(R.id.cerrar_sesion_user))
            popupMenu.menuInflater.inflate(R.menu.cerrar_sesion, popupMenu.menu)

            // Configura el menú para que se muestre hacia la derecha
            popupMenu.gravity = Gravity.END
            popupMenu.setForceShowIcon(true)

            // Establece un listener para manejar los clics en los elementos del menú
            popupMenu.setOnMenuItemClickListener { item ->
                try {
                    when (item.itemId) {
                        R.id.cerrar_sesion -> {
                            val intent = Intent(this@menu_principal_fragment.requireContext(), Login_menu::class.java)
                            startActivity(intent)
                            true
                        }
                        else -> false
                    }
                }catch (e: Exception) {
                // Manejo de excepciones: registrar el error si ocurre
                Log.e("menu_principal_fragment", "Error al manejar el menú de cerrar sesion: ${e.message}")
            }
            // Si no se ha manejado ninguna opción, devolver falso
            false
        }
            // Mostrar el menú emergente
            popupMenu.show()
    } catch (e: Exception) {
        // Manejo de excepciones durante la creación del PopupMenu
        Log.e("menu_principal_fragment", "Error al mostrar el menú emergente: ${e.message}")
        }

    }

    private fun obtenerUsuario() {
        val cookieManager = CookieManager()
        CookieHandler.setDefault(cookieManager)

        val url = getString(R.string.url) + "/obtener_nombre_usuario.php" // Replace with your endpoint
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Cargando...")
        progressDialog.show()

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            progressDialog.dismiss()
            try {
                Log.d("API Response", "Response: $response")
                val jsonObject = JSONObject(response)
                val success = jsonObject.getBoolean("success")
                if (success) {

                    val sacar_usuario = jsonObject.getString("nombre_usuario") // Replace with your key
                    val mostrar_usuario = view?.findViewById<TextView>(R.id.bienvenido)
                    mostrar_usuario?.text = "$sacar_usuario"
                } else {
                    val message = if (jsonObject.has("mensaje")) {
                        jsonObject.getString("mensaje")
                    } else {
                        "Ocurrió un error desconocido."
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("menu_principal_fragment", "Error al procesar los datos: ${e.message}")
            }
        }, { error ->
            progressDialog.dismiss()
            Log.e("menu_principal_fragment", "Error de red: ${error.message}")
        })

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    // Compañero de objeto para crear nuevas instancias del fragment
    companion object {
        // Constantes para los nombres de los parámetros
        const val NAME = "param1"
        const val ADDRESS = "param2"

        // Método estático para crear una nueva instancia del fragment con parámetros
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            menu_principal_fragment().apply {
                arguments = Bundle().apply {
                    // Almacenar los parámetros en un Bundle
                    putString(NAME, param1)
                    putString(ADDRESS, param2)
                }
            }
    }
}

