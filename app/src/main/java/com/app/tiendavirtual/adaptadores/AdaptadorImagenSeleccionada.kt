package com.app.tiendavirtual.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.tiendavirtual.R
import com.app.tiendavirtual.databinding.ItemImagenesSeleccionadasBinding
import com.app.tiendavirtual.modelos.ModeloImagenSeleccionada
import com.bumptech.glide.Glide

class AdaptadorImagenSeleccionada(
    private val context : Context,
    private val imagenesSelectArrayList : ArrayList<ModeloImagenSeleccionada>
): Adapter<AdaptadorImagenSeleccionada.HolderImagenSeleccionada>() {
    private lateinit var binding : ItemImagenesSeleccionadasBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderImagenSeleccionada {
        binding = ItemImagenesSeleccionadasBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderImagenSeleccionada(binding.root)
    }

    override fun getItemCount(): Int {
        return imagenesSelectArrayList.size
    }

    override fun onBindViewHolder(holder: HolderImagenSeleccionada, position: Int) {
        val modelo = imagenesSelectArrayList[position]
        val imagenUri = modelo.imageUri

        //Leer imagenes
        try {
            Glide.with(context)
                .load(imagenUri)
                .placeholder(R.drawable.item_imagen)
                .into(holder.imagenItem)
        } catch (e:Exception){

        }
        //Eliminar una imagen de la lista
        holder.btn_borrar.setOnClickListener {
            imagenesSelectArrayList.remove(modelo)
            notifyDataSetChanged()
        }
    }

    inner class HolderImagenSeleccionada(itemView : View) : ViewHolder(itemView){
        var imagenItem = binding.imagenItem
        var btn_borrar = binding.borrarItem
    }
}