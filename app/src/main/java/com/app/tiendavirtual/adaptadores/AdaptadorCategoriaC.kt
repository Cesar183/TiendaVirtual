package com.app.tiendavirtual.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.tiendavirtual.R
import com.app.tiendavirtual.databinding.ItemCategoriaCBinding
import com.app.tiendavirtual.modelos.ModeloCategoria
import com.bumptech.glide.Glide

class AdaptadorCategoriaC : RecyclerView.Adapter<AdaptadorCategoriaC.HolderCategoriaC> {

    private lateinit var binding :  ItemCategoriaCBinding

    private var mContext : Context
    private var categoriaArrayList : ArrayList<ModeloCategoria>

    constructor(mContext: Context, categoriaArrays: ArrayList<ModeloCategoria>) : super() {
        this.mContext = mContext
        this.categoriaArrayList = categoriaArrays
    }

    inner class HolderCategoriaC (itemView: View) : RecyclerView.ViewHolder(itemView){
        var item_nombreCat_c = binding.itemNombreCatC
        var item_img_cat = binding.imagenCateg
        var item_var_productos = binding.itemVerProductos


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaC {
        binding = ItemCategoriaCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaC(binding.root)
    }

    override fun getItemCount(): Int {
        return categoriaArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategoriaC, position: Int) {
        val modelo = categoriaArrayList[position]
        val categoria = modelo.categoria
        val imagen = modelo.imagenUrl

        holder.item_nombreCat_c.text = categoria

        Glide.with(mContext)
            .load(imagen)
            .placeholder(R.drawable.categorias)
            .into(holder.item_img_cat)
    }
}