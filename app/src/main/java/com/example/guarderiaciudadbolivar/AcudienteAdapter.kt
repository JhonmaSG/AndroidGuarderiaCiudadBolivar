package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.BaseAdapter
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
class AcudienteAdapter(
    private val context: Context,
    private val acudientes: List<Acudiente>
) : ArrayAdapter<Acudiente>(context, R.layout.item_acudiente, acudientes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_acudiente, parent, false)

        val acudiente = acudientes[position]
        view.findViewById<TextView>(R.id.txtNombre).text = acudiente.nombre
        view.findViewById<TextView>(R.id.txtCedula).text = acudiente.cedula.toString()
        return view
    }
}
