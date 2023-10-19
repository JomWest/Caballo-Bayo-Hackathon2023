package com.yonjar.njambox.Classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yonjar.njambox.Classes.Anuncio
import com.yonjar.njambox.R
import java.util.ArrayList

class AnuncioAdapter(private val imageList: ArrayList<Anuncio>, private val context: Context)
    : RecyclerView.Adapter<AnuncioAdapter.ImageViewHolder>(){

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.imgAdvert)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemimagen, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context).load(imageList[position].imagenAnuncio).into(holder.image)
    }
}