package com.app.tiendavirtual.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.tiendavirtual.databinding.ItemCategoriaVBinding
import com.app.tiendavirtual.modelos.ModeloCategoria
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

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
            //Toast.makeText(mContext, "Eliminar categoria", Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Eliminar categoría")
            builder.setMessage("¿Quieres eliminar esta categoría?")
                .setPositiveButton("Confirmar"){a,d ->
                    eliminarCategoria(modelo, holder)
                }
                .setNegativeButton("Cancelar"){a,d ->
                    a.dismiss()
                }
            builder.show()
        }
    }

    private fun eliminarCategoria(modelo: ModeloCategoria, holder: AdaptadorCategoriaV.HolderCategoriaV) {
        val idCat = modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.child(idCat).removeValue()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Categoría eliminada", Toast.LENGTH_SHORT).show()
                eliminarImgCat(idCat)
            }
            .addOnFailureListener {e ->
                Toast.makeText(mContext, "No se eliminó la categoria debido a ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun eliminarImgCat(idCat: String) {
        val nombreImg = idCat
        val rutaImagen = "Categorias/$nombreImg"
        val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageRef.delete()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Se eliminó la imagen de la categoría", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    inner class HolderCategoriaV(itemView : View) : RecyclerView.ViewHolder(itemView){
        var item_nombreCat_v = binding.itemNombreCatV
        var item_eliminarCat = binding.itemEliminarC
    }
}