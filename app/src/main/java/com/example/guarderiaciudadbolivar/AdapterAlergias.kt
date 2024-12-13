package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AdapterAlergias(
    context: Context,
    private val alergiasList: List<Alergias>
) : ArrayAdapter<Alergias>(context, R.layout.list_item_alergias, alergiasList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_alergias, parent, false)

        val tvNoMatricula = view.findViewById<TextView>(R.id.tvNoMatricula)
        val tvIngredienteId = view.findViewById<TextView>(R.id.tvIngredienteId)
        val tvObservaciones = view.findViewById<TextView>(R.id.tvObservaciones)

        val alergia = alergiasList[position]

        tvNoMatricula.text = "Matrícula: ${alergia.noMatricula}"
        tvIngredienteId.text = "Ingrediente: ${alergia.ingredienteId}"
        tvObservaciones.text = "Observaciones: ${alergia.observaciones}"

        return view
    }
}
