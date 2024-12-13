package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.guarderiaciudadbolivar.R
import android.widget.ArrayAdapter

class NinosAdapter(context: Context, private val ninos: ArrayList<Nino>) : ArrayAdapter<Nino>(context, 0, ninos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_nino, parent, false)

        val nino = ninos[position]

        val nombreTextView: TextView = view.findViewById(R.id.tvNombre)
        val fechaNacimientoTextView: TextView = view.findViewById(R.id.tvFechaNacimiento)
        val estadoTextView: TextView = view.findViewById(R.id.tvEstado)

        nombreTextView.text = nino.nombre
        fechaNacimientoTextView.text = nino.fechaNacimiento
        estadoTextView.text = nino.estado

        return view
    }
}