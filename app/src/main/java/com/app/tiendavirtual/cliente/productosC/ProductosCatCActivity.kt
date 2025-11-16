package com.app.tiendavirtual.cliente.productosC

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.tiendavirtual.R
import com.app.tiendavirtual.adaptadores.AdaptadorProductosC
import com.app.tiendavirtual.databinding.ActivityProductosCatCactivityBinding
import com.app.tiendavirtual.modelos.ModeloProducto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductosCatCActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductosCatCactivityBinding
    private lateinit var productoArrayList : ArrayList<ModeloProducto>
    private lateinit var adaptadorProductos : AdaptadorProductosC
    private var nombreCat = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosCatCactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtener nombre de la categoria
        nombreCat = intent.getStringExtra("nombreCat").toString()
        listarProductos(nombreCat)
    }

    private fun listarProductos(nombreCat: String) {
        productoArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.orderByChild("categoria").equalTo(nombreCat)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    productoArrayList.clear()
                    for (ds in snapshot.children){
                        val modeloProducto = ds.getValue(ModeloProducto::class.java)
                        productoArrayList.add(modeloProducto!!)
                    }
                    adaptadorProductos = AdaptadorProductosC(this@ProductosCatCActivity, productoArrayList)
                    binding.productosRV.adapter = adaptadorProductos
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}