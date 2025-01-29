package com.example.guarderiaciudadbolivar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallesAcudiente : AppCompatActivity() {

    private lateinit var txtCedula: TextView
    private lateinit var txtNombre: TextView
    private lateinit var txtDireccion: TextView
    private lateinit var txtParentesco: TextView
    private lateinit var txtNumeroCuenta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_acudiente)

        // Vincular vistas con sus IDs
        txtCedula = findViewById(R.id.txtCedula)
        txtNombre = findViewById(R.id.txtNombre)
        txtDireccion = findViewById(R.id.txtDireccion)
        txtParentesco = findViewById(R.id.txtParentesco)
        txtNumeroCuenta = findViewById(R.id.txtNumeroCuenta)

        // Obtener datos desde el Intent
        val cedula = intent.getStringExtra("cedula")
        val nombre = intent.getStringExtra("nombre")
        val direccion = intent.getStringExtra("direccion")
        val parentesco = intent.getStringExtra("parentesco")
        val numeroCuenta = intent.getStringExtra("numeroCuenta")

        // Log para ver qué datos están llegando
        Log.d("DetallesAcudiente", "Cedula: $cedula")
        Log.d("DetallesAcudiente", "Nombre: $nombre")
        Log.d("DetallesAcudiente", "Direccion: $direccion")
        Log.d("DetallesAcudiente", "Parentesco: $parentesco")
        Log.d("DetallesAcudiente", "Numero Cuenta: $numeroCuenta")

        // Mostrar los detalles del acudiente si están disponibles
        txtCedula.text = "Cédula: ${cedula ?: "No disponible"}"
        txtNombre.text = "Nombre: ${nombre ?: "No disponible"}"
        txtDireccion.text = "Dirección: ${direccion ?: "No disponible"}"
        txtParentesco.text = "Parentesco: ${parentesco ?: "No disponible"}"
        txtNumeroCuenta.text = "Número de Cuenta: ${numeroCuenta ?: "No disponible"}"
    }
}
