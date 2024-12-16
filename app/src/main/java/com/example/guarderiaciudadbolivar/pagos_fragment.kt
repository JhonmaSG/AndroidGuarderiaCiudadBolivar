package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class pagos_fragment : Fragment() {

    // Declarar las variables para los componentes
    private lateinit var btnVerPagos: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragment
        return inflater.inflate(R.layout.fragment_pagos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar la variable para el botón
        btnVerPagos = view.findViewById(R.id.btnVerPagos)

        // Acción para el botón
        btnVerPagos.setOnClickListener {
            // Redirigir a un enlace web
            val url = "https://zieete.com.co/guarderia/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
