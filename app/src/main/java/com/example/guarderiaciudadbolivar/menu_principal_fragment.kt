package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class menu_principal_fragment : Fragment() {
    // Variables privadas para almacenar parámetros pasados al fragment (opcionales)
    private var name: String? = null
    private var address: String? = null

    // Método llamado al crear el fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Recuperar los argumentos pasados al fragment
            arguments?.let {
                // Obtener el nombre y la dirección de los argumentos
                name = it.getString(NAME)
                address = it.getString(ADDRESS)
                // Registrar el nombre en el log para depuración
                Log.i("aris", name.orEmpty())
            }
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

    // Lógica adicional del Fragment puede ser implementada aquí
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            // Aquí puedes acceder a los elementos de la vista y definir su comportamiento.
            // Por ejemplo, puedes inicializar adaptadores, establecer listeners, etc.

            // Ejemplo de lógica:
            // val button = view.findViewById<Button>(R.id.my_button)
            // button.setOnClickListener {
            //     // Acciones a realizar cuando se presiona el botón
            // }
        } catch (e: Exception) {
            // Manejo de excepciones para la lógica del view
            Log.e("menu_principal_fragment", "Error en onViewCreated: ${e.message}")
        }
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

