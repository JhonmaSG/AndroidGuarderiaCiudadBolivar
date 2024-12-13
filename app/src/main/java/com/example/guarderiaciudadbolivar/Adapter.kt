package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class Adapter(
    context: Context,
    private val arrayIngredientes: List<Ingredientes>
) : ArrayAdapter<Ingredientes>(context, R.layout.list_ingredientes, arrayIngredientes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_ingredientes, parent, false)

        // Obtener los TextViews del diseño
        val tvid = view.findViewById<TextView>(R.id.txtid)
        val tnombre = view.findViewById<TextView>(R.id.txtnombre)

        // Obtener el ingrediente actual
        val ingrediente = arrayIngredientes[position]

        // Configurar los valores en las vistas
        tvid.text = ingrediente.ingredienteId
        tnombre.text = ingrediente.nombreIngrediente

        return view
    }
}
