package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


// Constantes para los argumentos del Fragment (opcional)
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// Definición de la clase del Fragment
class historial_fragment : Fragment() {
    // Variables para almacenar los parámetros que se pasan al Fragment (opcionales)
    private var param1: String? = null
    private var param2: String? = null

    // Método llamado al crear el Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Recupera los argumentos si están presentes
            arguments?.let {
                param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
            }
        } catch (e: Exception) {
            // Manejo de excepciones durante la recuperación de argumentos
            Log.e("historial_fragment", "Error al recuperar argumentos: ${e.message}")
        }
    }

    // Método que infla el diseño del Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout correspondiente al Fragment
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    // Método estático para crear una nueva instancia del Fragment con parámetros
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            historial_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1) // Almacena el primer parámetro
                    putString(ARG_PARAM2, param2) // Almacena el segundo parámetro
                }
            }
    }

    // Lógica adicional del Fragment puede ser implementada aquí
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Aquí puedes acceder a los elementos de la vista y definir su comportamiento.
        // Por ejemplo, puedes inicializar adaptadores, establecer listeners, etc.

        // Ejemplo de lógica:
        // val button = view.findViewById<Button>(R.id.my_button)
        // button.setOnClickListener {
        //     // Acciones a realizar cuando se presiona el botón
        // }
    }
}
