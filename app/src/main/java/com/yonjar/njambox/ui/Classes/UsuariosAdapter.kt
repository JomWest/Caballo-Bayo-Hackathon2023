package com.yonjar.njambox.ui.Classes

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yonjar.njambox.Classes.AnuncioAdapter

import com.yonjar.njambox.Classes.Usuarios
import com.yonjar.njambox.R
import java.util.ArrayList

class UsuariosAdapter(private val perfilList: ArrayList<Usuarios>, private val context: Context)
    : RecyclerView.Adapter<UsuariosAdapter.perfilViewHolder>() {

    class perfilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val tvProfileName:TextView = itemView.findViewById(R.id.tvNombreCantante)
        private val ivProfileImage:ImageView = itemView.findViewById(R.id.ivPerfilCantante)
        fun render(Usuario:Usuarios){
            tvProfileName.text = Usuario.userName
            ivProfileImage.setImageURI(Usuario.profileImage.toUri())
        }
        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsuariosAdapter.perfilViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.perfil_artista, parent, false)
        return UsuariosAdapter.perfilViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return perfilList.size
    }

    override fun onBindViewHolder(holder: UsuariosAdapter.perfilViewHolder, position: Int) {
holder.render(perfilList[position])
}
}