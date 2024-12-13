package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// Constantes para los nombres de los parámetros
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class fragment_creditos : Fragment() {
    // Variables privadas para almacenar parámetros pasados al fragment (opcionales)
    private var param1: String? = null
    private var param2: String? = null

    // Método llamado al crear el fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Recuperar los argumentos pasados al fragment
            arguments?.let {
                // Obtener el valor de los parámetros
                param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
            }
        } catch (e: Exception) {
            // Manejo de excepciones durante la recuperación de argumentos
            Log.e("fragment_creditos", "Error al recuperar argumentos: ${e.message}")
        }
    }

    // Método para inflar la vista del fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout correspondiente al fragment
        return inflater.inflate(R.layout.fragment_creditos, container, false)
    }

    // Compañero de objeto para crear nuevas instancias del fragment
    companion object {
        // Método estático para crear una nueva instancia del fragment con parámetros
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_creditos().apply {
                arguments = Bundle().apply {
                    // Almacenar los parámetros en un Bundle
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
