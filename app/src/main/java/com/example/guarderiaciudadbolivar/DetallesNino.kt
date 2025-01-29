package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallesNino : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_nino)

        val tvNoMatricula = findViewById<TextView>(R.id.tvNoMatricula)
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvAcudiente = findViewById<TextView>(R.id.tvAcudiente)
        val tvFechaNacimiento = findViewById<TextView>(R.id.tvFechaNacimiento)
        val tvFechaIngreso = findViewById<TextView>(R.id.tvFechaIngreso)
        val tvEstado = findViewById<TextView>(R.id.tvEstado)

        val nino = intent.getSerializableExtra("nino") as? Nino

        nino?.let {
            tvNoMatricula.text = "Matrícula: ${it.noMatricula}"
            tvNombre.text = "Nombre: ${it.nombre}"
            tvAcudiente.text = "Acudiente: ${it.acudienteCedula}"
            tvFechaNacimiento.text = "Fecha Nacimiento: ${it.fechaNacimiento}"
            tvFechaIngreso.text = "Fecha Ingreso: ${it.fechaIngreso}"
            tvEstado.text = "Estado: ${it.estado}"
        }
    }
}
