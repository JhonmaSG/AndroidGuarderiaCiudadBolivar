package com.example.guarderiaciudadbolivar
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guarderiaciudadbolivar.R

class AlergiasAdapter(private val context: Context, private val alergias: ArrayList<Alergia>) : RecyclerView.Adapter<AlergiasAdapter.AlergiaViewHolder>(),
    ListAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlergiaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_alergia, parent, false)
        return AlergiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlergiaViewHolder, position: Int) {
        val alergia = alergias[position]
        holder.bind(alergia)
    }

    override fun getItemCount(): Int {
        return alergias.size
    }

    inner class AlergiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private val ingredienteId: TextView = itemView.findViewById(R.id.tvIngredienteId)
        //private val observaciones: TextView = itemView.findViewById(R.id.tvObservaciones)

        fun bind(alergia: Alergia) {
            //ingredienteId.text = alergia.ingredienteId.toString()
            //observaciones.text = alergia.observaciones
        }
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun areAllItemsEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(p0: Int): Boolean {
        TODO("Not yet implemented")
    }
}