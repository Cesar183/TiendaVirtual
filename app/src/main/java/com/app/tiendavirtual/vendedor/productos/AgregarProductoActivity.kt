package com.app.tiendavirtual.vendedor.productos

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.app.tiendavirtual.Constantes
import com.app.tiendavirtual.R
import com.app.tiendavirtual.adaptadores.AdaptadorImagenSeleccionada
import com.app.tiendavirtual.databinding.ActivityAgregarProductoBinding
import com.app.tiendavirtual.modelos.ModeloImagenSeleccionada
import com.github.dhaval2404.imagepicker.ImagePicker

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imagenUri : Uri?=null

    private lateinit var imagenSeleccionadaArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSel : AdaptadorImagenSeleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagenSeleccionadaArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }

        cargarImagenes()
    }

    private fun cargarImagenes() {
        adaptadorImagenSel = AdaptadorImagenSeleccionada(this, imagenSeleccionadaArrayList)
        binding.RVImagenesProducto.adapter = adaptadorImagenSel
    }

    private fun seleccionarImg(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
            if (resultado.resultCode == Activity.RESULT_OK){
                val data = resultado.data
                imagenUri = data!!.data
                val tiempo = "${Constantes().obtenerTiempo()}"
                val modelImgSel = ModeloImagenSeleccionada(tiempo, imagenUri, null, false)
                imagenSeleccionadaArrayList.add(modelImgSel)
                cargarImagenes()
            } else {
                Toast.makeText(this, "Acción cancelada", Toast.LENGTH_SHORT).show()
            }
        }
}