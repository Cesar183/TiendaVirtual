package com.app.tiendavirtual.vendedor.productos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.tiendavirtual.Constantes
import com.app.tiendavirtual.R
import com.app.tiendavirtual.adaptadores.AdaptadorImagenSeleccionada
import com.app.tiendavirtual.databinding.ActivityAgregarProductoBinding
import com.app.tiendavirtual.modelos.ModeloCategoria
import com.app.tiendavirtual.modelos.ModeloImagenSeleccionada
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private var imagenUri : Uri?=null

    private lateinit var imagenSeleccionadaArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSel : AdaptadorImagenSeleccionada
    private lateinit var categoriasArrayList : ArrayList<ModeloCategoria>
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarCategorias()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        //Ocultar Descuento y nota
        binding.etPrecionConDescuentoP.visibility = View.GONE
        binding.etNotaDescuentoP.visibility = View.GONE

        //Mostrar y ocultar los EditText
        binding.descuentoSwitch.setOnCheckedChangeListener{buttonView, isCheked ->
            if (isCheked){
                binding.etPrecionConDescuentoP.visibility = View.VISIBLE
                binding.etNotaDescuentoP.visibility = View.VISIBLE
            } else {
                binding.etPrecionConDescuentoP.visibility = View.GONE
                binding.etNotaDescuentoP.visibility = View.GONE
            }
        }

        imagenSeleccionadaArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }

        binding.categoria.setOnClickListener {
            selecCategorias()
        }

        binding.btnAgregarProducto.setOnClickListener {
            validarInfo()
        }

        cargarImagenes()
    }

    private var nombreP = ""
    private var descripcionP = ""
    private var categoriaP = ""
    private var precioP = ""
    private var descuentoHab = false
    private var precioDescP = ""
    private var notaDescP = ""
    private fun validarInfo() {
        nombreP = binding.etNombresP.text.toString().trim()
        descripcionP = binding.etDescripcionP.text.toString().trim()
        categoriaP = binding.categoria.text.toString().trim()
        precioP = binding.etPrecioP.text.toString().trim()
        descuentoHab = binding.descuentoSwitch.isChecked

        if(nombreP.isEmpty()){
            binding.etNombresP.error = "ingrese nombre"
            binding.etNombresP.requestFocus()
        } else if (descripcionP.isEmpty()){
            binding.etDescripcionP.error = "Ingrese descripción"
            binding.etDescripcionP.requestFocus()
        } else if (categoriaP.isEmpty()){
            binding.categoria.error = "Seleccione una categoría"
            binding.categoria.requestFocus()
        } else if (precioP.isEmpty()){
            binding.etPrecioP.error = "Ingrese precio"
            binding.etPrecioP.requestFocus()
        } else if (imagenUri == null){
            Toast.makeText(this, "Seleccione al menos una imagen", Toast.LENGTH_SHORT).show()
        } else if (descuentoHab) {
            precioDescP = binding.etPrecionConDescuentoP.text.toString().trim()
            notaDescP = binding.etNotaDescuentoP.text.toString().trim()
            if (precioDescP.isEmpty()){
                binding.etPrecionConDescuentoP.error = "Ingrese precio con descuento"
                binding.etPrecionConDescuentoP.requestFocus()
            } else if (notaDescP.isEmpty()){
                binding.etNotaDescuentoP.text.toString().trim()
                binding.etNotaDescuentoP.requestFocus()
            } else {
                agregarProducto()
            }
        } else {
            precioDescP = "0"
            notaDescP = ""
            agregarProducto()
        }
    }

    private fun agregarProducto() {
        progressDialog.setMessage("Agregando producto")
        progressDialog.show()

        var ref = FirebaseDatabase.getInstance().getReference("Productos")
        val keyId = ref.push().key
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "${keyId}"
        hashMap["nombre"] = "${nombreP}"
        hashMap["descripcion"] = "${descripcionP}"
        hashMap["categoria"] = "${categoriaP}"
        hashMap["precio"] = "${precioP}"
        hashMap["precioDesc"] = "${precioDescP}"
        hashMap["notaDesc"] = "${notaDescP}"

        ref.child(keyId!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                subirImgStorage(keyId)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subirImgStorage(keyId: String) {
        for (i in imagenSeleccionadaArrayList.indices){
            val modeloImagenSel = imagenSeleccionadaArrayList[i]
            val nombreImagen = modeloImagenSel.id
            val rutaImagen = "Productos/$nombreImagen"
            val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
            storageRef.putFile(modeloImagenSel.imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val urlImgCargada = uriTask.result
                    if (uriTask.isSuccessful){
                        val hashMap = HashMap<String, Any>()
                        hashMap["id"] = "${modeloImagenSel.id}"
                        hashMap["imagenUrl"] = "${urlImgCargada}"

                        val ref = FirebaseDatabase.getInstance().getReference("Productos")
                        ref.child(keyId).child("Imagenes")
                            .child(nombreImagen)
                            .updateChildren(hashMap)
                        progressDialog.dismiss()
                        Toast.makeText(this, "Se agregó el producto", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    }
                }
                .addOnFailureListener{e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun limpiarCampos() {
        imagenSeleccionadaArrayList.clear()
        adaptadorImagenSel.notifyDataSetChanged()
        binding.etNombresP.setText("")
        binding.etDescripcionP.setText("")
        binding.etPrecioP.setText("")
        binding.categoria.setText("")
        binding.descuentoSwitch.isChecked = false
        binding.etPrecionConDescuentoP.setText("")
        binding.etNotaDescuentoP.setText("")
    }

    private fun cargarCategorias() {
        categoriasArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriasArrayList.clear()
                for (ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriasArrayList.add(modelo!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private var idCat = ""
    private var tituloCat = ""
    private fun selecCategorias(){
        val categoriasArray = arrayOfNulls<String>(categoriasArrayList.size)
        for (i in categoriasArray.indices){
            categoriasArray[i] = categoriasArrayList[i].categoria
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione una categoría")
            .setItems(categoriasArray){dialog, witch ->
                idCat = categoriasArrayList[witch].id
                tituloCat = categoriasArrayList[witch].categoria
                binding.categoria.text = tituloCat
            }
            .show()
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