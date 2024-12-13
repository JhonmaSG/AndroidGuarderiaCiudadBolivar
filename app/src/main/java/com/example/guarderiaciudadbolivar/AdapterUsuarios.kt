package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AdapterUsuarios(
    context: Context,
    private val arrayUsuarios: List<Usuarios>
) : ArrayAdapter<Usuarios>(context, R.layout.list_usuarios, arrayUsuarios) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_usuarios, parent, false)

        // Obtener los TextViews del diseño
        val tvid = view.findViewById<TextView>(R.id.txtid)
        val tnombre = view.findViewById<TextView>(R.id.txtnombre)

        // Obtener el ingrediente actual
        val ingrediente = arrayUsuarios[position]

        // Configurar los valores en las vistas
        tvid.text = ingrediente.usuarioId
        tnombre.text = ingrediente.nombreUsuario

        return view
    }
}
