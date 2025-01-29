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
) : ArrayAdapter<Alergias>(context, R.layout.list_alergias_item, alergiasList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_alergias_item, parent, false)

        val tvNoMatricula = view.findViewById<TextView>(R.id.txtNoMatricula)
        val tvIngredienteId = view.findViewById<TextView>(R.id.txtIngrediente)
        val tvObservaciones = view.findViewById<TextView>(R.id.txtObservaciones)

        val alergia = alergiasList[position]

        tvNoMatricula.text = "Matrícula: ${alergia.noMatricula}"
        tvIngredienteId.text = "Ingrediente: ${alergia.ingredienteId}"
        tvObservaciones.text = "Observaciones: ${alergia.observaciones}"

        return view
    }
}
