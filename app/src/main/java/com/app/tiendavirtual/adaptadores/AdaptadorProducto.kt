package com.app.tiendavirtual.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.tiendavirtual.R
import com.app.tiendavirtual.databinding.ItemProductoBinding
import com.app.tiendavirtual.modelos.ModeloProducto
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProducto : RecyclerView.Adapter<AdaptadorProducto.HolderProducto> {

    private lateinit var binding: ItemProductoBinding

    private var mContext : Context
    private var productosArrayList : ArrayList<ModeloProducto>

    constructor(mContext: Context, ProductosArrayList: ArrayList<ModeloProducto>) {
        this.mContext = mContext
        this.productosArrayList = ProductosArrayList
    }

    inner class HolderProducto(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imagenP = binding.imagenP
        var item_nombre_p = binding.itemNombreP
        var item_precio_p = binding.itemPrecioP
        var item_precio_p_desc = binding.itemPrecioPDesc
        var item_nota_p = binding.itemNotaP
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProducto {
        binding = ItemProductoBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProducto(binding.root)
    }

    override fun getItemCount(): Int {
        return productosArrayList.size
    }

    override fun onBindViewHolder(holder: HolderProducto, position: Int) {
        val modeloProducto = productosArrayList[position]
        val nombre = modeloProducto.nombre
        val precio = modeloProducto.precio
        val precioDesc = modeloProducto.precioDesc
        val notaDesc = modeloProducto.notaDesc

        cargarPrimeraImg(modeloProducto, holder)

        holder.item_nombre_p.text = "${nombre}"
        holder.item_precio_p.text = "${precio}${" USD"}"
        holder.item_precio_p_desc.text = "${precioDesc}"
        holder.item_nota_p.text = "${notaDesc}"

        if (precioDesc.isNotEmpty() && notaDesc.isNotEmpty()){
            visualizarDescuento(modeloProducto, holder)
        }
    }

    private fun visualizarDescuento(modeloProducto: ModeloProducto, holder: AdaptadorProducto.HolderProducto) {
        val idProducto = modeloProducto.id
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val nota_desc = "${snapshot.child("notaDesc").value}"
                val precio_desc = "${snapshot.child("precioDesc").value}"
                if (nota_desc.isNotEmpty() && precio_desc.isNotEmpty()){
                    holder.item_nota_p.visibility = View.VISIBLE
                    holder.item_precio_p_desc.visibility = View.VISIBLE

                    holder.item_nota_p.text = "${nota_desc}"
                    holder.item_precio_p_desc.text = "${precio_desc}${" USD"}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun cargarPrimeraImg(modeloProducto: ModeloProducto, holder: AdaptadorProducto.HolderProducto) {
        val idProducto = modeloProducto.id
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        val imagenUrl = "${ds.child("imagenUrl").value}"
                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.item_imagen_producto)
                                .into(holder.imagenP)
                        } catch (e:Exception){

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}