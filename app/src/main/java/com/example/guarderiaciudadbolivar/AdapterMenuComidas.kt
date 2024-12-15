package com.example.guarderiaciudadbolivar

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterMenuComidas(
    context: Context,
    private val ArrayMenuList: List<MenuComidas>
) : ArrayAdapter<MenuComidas>(context, R.layout.list_menu_comidas, ArrayMenuList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_menu_comidas, parent, false)

        // Obtener los TextViews del diseño
        val tvid = view.findViewById<TextView>(R.id.txtMenuId)
        val tnombre = view.findViewById<TextView>(R.id.txtMenuNombre)

        // Obtener el menú actual
        val menu = ArrayMenuList[position]

        tvid.text = menu.menuId
        tnombre.text = menu.nombreMenu

        return view
    }
}

