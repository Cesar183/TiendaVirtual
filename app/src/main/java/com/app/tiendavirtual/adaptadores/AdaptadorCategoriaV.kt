package com.app.tiendavirtual.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.tiendavirtual.databinding.ItemCategoriaVBinding
import com.app.tiendavirtual.modelos.ModeloCategoria

class AdaptadorCategoriaV: RecyclerView.Adapter<AdaptadorCategoriaV.HolderCategoriaV> {

    private lateinit var binding: ItemCategoriaVBinding

    private val mContext : Context
    private val categoriaArrayList: ArrayList<ModeloCategoria>

    constructor(mContext: Context, categoriaArrayList: ArrayList<ModeloCategoria>){
        this.mContext = mContext
        this.categoriaArrayList = categoriaArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaV {
        binding = ItemCategoriaVBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaV(binding.root)
    }

    override fun getItemCount(): Int {
        return categoriaArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategoriaV, position: Int) {
        val modelo = categoriaArrayList[position]
        val id = modelo.id
        val categoria = modelo.categoria

        holder.item_nombreCat_v.text = categoria
        holder.item_eliminarCat.setOnClickListener {
            Toast.makeText(mContext, "Eliminar categoria", Toast.LENGTH_SHORT).show()
        }
    }

    inner class HolderCategoriaV(itemView : View) : RecyclerView.ViewHolder(itemView){
        var item_nombreCat_v = binding.itemNombreCatV
        var item_eliminarCat = binding.itemEliminarC
    }
}