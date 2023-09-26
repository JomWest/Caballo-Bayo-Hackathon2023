package com.yonjar.newjambox.Classes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yonjar.newjambox.MainActivity
import com.yonjar.newjambox.databinding.CardviewsongBinding

class CancionesAdapter(val canciones:List<Cancion>, val con:MainActivity):
    RecyclerView.Adapter<CancionesAdapter.ViewHolder>()
{

    var selected = -1
    class ViewHolder(val bind: CardviewsongBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = CardviewsongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cancion = canciones[position]
        with(holder.bind){
            tvNombreCancion.text = cancion.nombreCancion
            tvNombreArtista.text = cancion.nombreArtista
            imageView.setImageResource(cancion.imagenCancion)
            if(position==selected){
                LayCancion.setBackgroundColor(Color.rgb(100,100,100))
            }
            else{
                LayCancion.setBackgroundColor(Color.rgb(0,0,0))
            }
            LayCancion.setOnClickListener {
                con.cancionActualIndex = position
                con.resetCancion()
                selected = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return canciones.size
    }
}