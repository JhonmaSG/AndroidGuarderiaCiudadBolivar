package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ModuloNinosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modulo_ninos, container, false)

        // Referencia al botón
        val btnIrNinos = view.findViewById<Button>(R.id.btnIrModuloNinos)
        btnIrNinos.setOnClickListener {
            // Abre la actividad que contiene la gestión de niños
            val intent = Intent(requireContext(), NinoFragment::class.java)
            startActivity(intent)
        }

        return view
    }
}
