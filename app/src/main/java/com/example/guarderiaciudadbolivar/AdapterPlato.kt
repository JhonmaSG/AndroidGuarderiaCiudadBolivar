package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AdapterPlato(
    context: Context,
    private val platosList: List<Plato>
) : ArrayAdapter<Plato>(context, R.layout.list_platos, platosList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_platos, parent, false)

        //val tvid = view.findViewById<TextView>(R.id.txtPlatoId)
        val tnombre = view.findViewById<TextView>(R.id.txtNombrePlato)

        val plato = platosList[position]
        //tvid.text = plato.platoId
        tnombre.text = plato.nombrePlato

        return view
    }
}