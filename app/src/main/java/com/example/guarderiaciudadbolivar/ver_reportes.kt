package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// Claves para los argumentos que se pasan al fragmento
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// Clase que representa el fragmento de inscripciones
class ver_reportes : Fragment() {
    // Propiedades opcionales que almacenan los parámetros
    private var param1: String? = null
    private var param2: String? = null

    // Método llamado al crear el fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Recupera los argumentos pasados al fragmento
            arguments?.let {
                param1 = it.getString(ARG_PARAM1) // Asigna el valor de ARG_PARAM1 a param1
                param2 = it.getString(ARG_PARAM2) // Asigna el valor de ARG_PARAM2 a param2
            }
        } catch (e: Exception) {
            // Manejo de excepciones al recuperar argumentos
            Log.e("menu_inscripciones_fragment", "Error al recuperar argumentos: ${e.message}")
        }
    }

    // Método que infla la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Devuelve la vista inflada desde el layout XML correspondiente
        return inflater.inflate(R.layout.fragment_ver_reportes, container, false)
    }

    // Método donde se puede manejar la lógica del fragmento
    // Aquí puedes agregar interacciones con la vista o lógica adicional
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Implementar la lógica de interacción con elementos de la vista aquí
        // Ejemplo: configurar un botón o un TextView usando param1 y param2
        try {
            // Ejemplo de interacción: mostrar los parámetros en un TextView
            //val textView: TextView = view.findViewById(R.id.textView)
            //textView.text = "Parámetro 1: $param1, Parámetro 2: $param2"
        } catch (e: Exception) {
            // Manejo de excepciones al acceder a elementos de la vista
            Log.e("menu_inscripciones_fragment", "Error al establecer el texto: ${e.message}")
        }
    }

    companion object {
        // Método estático para crear una nueva instancia del fragmento con argumentos
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            menu_inscripciones_fragment().apply {
                // Almacena los parámetros en un Bundle
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1) // Agrega param1 al Bundle
                    putString(ARG_PARAM2, param2) // Agrega param2 al Bundle
                }
            }
    }
}