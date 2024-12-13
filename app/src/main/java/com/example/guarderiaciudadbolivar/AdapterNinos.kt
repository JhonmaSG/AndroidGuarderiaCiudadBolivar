package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AdapterNinos(
    context: Context,
    private val listaNinos: List<Nino>
) : ArrayAdapter<Nino>(context, R.layout.list_item_nino, listaNinos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_nino, parent, false)

        val txtNoMatricula = view.findViewById<TextView>(R.id.txtNoMatricula)
        val txtNombre = view.findViewById<TextView>(R.id.txtNombre)

        val nino = listaNinos[position]

        txtNoMatricula.text = "Matrícula: ${nino.noMatricula}"
        txtNombre.text = "Nombre: ${nino.nombre}"

        return view
    }
}
