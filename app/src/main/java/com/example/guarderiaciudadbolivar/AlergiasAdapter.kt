package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AlergiasAdapter(
    context: Context,
    private val alergiasList: ArrayList<Alergias>
) : ArrayAdapter<Alergias>(context, 0, alergiasList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.list_alergias_item,
            parent,
            false
        )

        val currentAlergia = alergiasList[position]

        val txtNoMatricula = view.findViewById<TextView>(R.id.txtNoMatricula)
        val txtIngrediente = view.findViewById<TextView>(R.id.txtIngrediente)
        val txtObservaciones = view.findViewById<TextView>(R.id.txtObservaciones)

        txtNoMatricula.text = "No. Matrícula: ${currentAlergia.noMatricula}"
        txtIngrediente.text = "Ingrediente: ${currentAlergia.ingredienteId}"
        txtObservaciones.text = "Observaciones: ${currentAlergia.observaciones}"

        return view
    }
}