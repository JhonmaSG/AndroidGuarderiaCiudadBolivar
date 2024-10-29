package com.example.guarderiaciudadbolivar

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }
}
