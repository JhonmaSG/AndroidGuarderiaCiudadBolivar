package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class NinoAdapter(private val context: Context, private val ninosList: ArrayList<Nino>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun getCount(): Int = ninosList.size

    override fun getItem(position: Int): Any = ninosList[position]

    override fun getItemId(position: Int): Long = ninosList[position].noMatricula.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_nino, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val nino = ninosList[position]
        holder.tvNombre.text = nino.nombre
        holder.tvNoMatricula.text = "Matrícula: ${nino.noMatricula}"
        holder.tvEstado.text = "Estado: ${nino.estado}"
        holder.tvFechaNacimiento.text = nino.fechaNacimiento?.let {
            "Fecha Nac: ${dateFormat.format(it)}"
        } ?: "Fecha Nac: N/A"

        return view
    }

    private class ViewHolder(view: View) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvNoMatricula: TextView = view.findViewById(R.id.tvNoMatricula)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvFechaNacimiento: TextView = view.findViewById(R.id.tvFechaNacimiento)
    }
}

