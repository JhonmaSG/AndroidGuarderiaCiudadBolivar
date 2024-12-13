package com.example.guarderiaciudadbolivar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlergiasActivity : AppCompatActivity() {

    private lateinit var btnAgregarAlergia: Button
    private lateinit var btnEditarAlergia: Button
    private lateinit var btnEliminarAlergia: Button
    private lateinit var btnConsultarAlergias: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alergias)

        // Inicializar botones
        btnAgregarAlergia = findViewById(R.id.btnAgregarAlergia)
        btnEditarAlergia = findViewById(R.id.btnEditarAlergia)
        btnEliminarAlergia = findViewById(R.id.btnEliminarAlergia)
        btnConsultarAlergias = findViewById(R.id.btnConsultarAlergias)

        // Configurar los botones con sus respectivas acciones
        btnAgregarAlergia.setOnClickListener {
            // Llamar a una función o actividad para agregar una alergia
            val intent = Intent(this, AgregarAlergiaFragment ::class.java)
            startActivity(intent)
        }

        btnConsultarAlergias.setOnClickListener {
            // Llamar a una función o actividad para consultar las alergias
            val intent = Intent(this, AlergiasFragment::class.java)
            startActivity(intent)
        }
    }
}
