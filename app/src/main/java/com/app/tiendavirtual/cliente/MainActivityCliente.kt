package com.app.tiendavirtual.cliente

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.app.tiendavirtual.R
import com.app.tiendavirtual.SeleccionarTipoActivity
import com.app.tiendavirtual.cliente.bottom_nav_fragments_cliente.FragmentMisOrdenesC
import com.app.tiendavirtual.cliente.bottom_nav_fragments_cliente.FragmentTiendaC
import com.app.tiendavirtual.cliente.nav_fragments_cliente.FragmentInicioC
import com.app.tiendavirtual.cliente.nav_fragments_cliente.FragmentMiPerfilC
import com.app.tiendavirtual.databinding.ActivityMainClienteBinding
import com.app.tiendavirtual.localizacion.FragmentLocation
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding
    private var firebaseAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()
        binding.navigationView.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawner
        )
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        replaceFragment(FragmentInicioC())
    }

    private fun comprobarSesion(){
        if (firebaseAuth!!.currentUser==null){
            startActivity(Intent(this@MainActivityCliente, SeleccionarTipoActivity::class.java))
            finishAffinity()
        } else {
            Toast.makeText(this, "Usuario en linea", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(this@MainActivityCliente, SeleccionarTipoActivity::class.java))
        finishAffinity()
        Toast.makeText(this, "Cerrando sesión", Toast.LENGTH_SHORT).show()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment,fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.op_inicio_c->{
                replaceFragment(FragmentInicioC())
            }
            R.id.op_mi_perfil_c->{
                replaceFragment(FragmentMiPerfilC())
            }
            R.id.op_cerrar_sesion_c->{
                cerrarSesion()
            }
            R.id.op_tienda_c->{
                replaceFragment(FragmentTiendaC())
            }
            R.id.op_mis_ordenes_c->{
                replaceFragment(FragmentMisOrdenesC())
            }
            R.id.op_geo_v->{
                replaceFragment(FragmentLocation())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}