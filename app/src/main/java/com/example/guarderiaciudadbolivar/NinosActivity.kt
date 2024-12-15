package com.example.guarderiaciudadbolivar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NinosActivity : AppCompatActivity() {

    private lateinit var btnAgregar: Button
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnConsultar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ninos) // Nombre del layout XML

        // Inicializar botones
        btnAgregar = findViewById(R.id.btnAgregar)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnConsultar = findViewById(R.id.btnConsultar)

        // Configurar acciones para cada botón
        btnAgregar.setOnClickListener {
            // Abrir la actividad o fragmento para agregar un niño
            val intent = Intent(this, AgregarNinoFragment::class.java)
            startActivity(intent)
        }

        btnEditar.setOnClickListener {
            // Abrir la actividad o fragmento para editar un niño
            val intent = Intent(this, AgregarNinoFragment::class.java)
            startActivity(intent)
        }

        btnConsultar.setOnClickListener {
            // Abrir la actividad o fragmento para consultar niños
            val intent = Intent(this, DetallesNinoFragment::class.java)
            startActivity(intent)
        }
    }
}
