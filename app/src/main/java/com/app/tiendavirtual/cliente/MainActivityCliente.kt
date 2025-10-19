package com.app.tiendavirtual.cliente

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.app.tiendavirtual.R
import com.app.tiendavirtual.cliente.bottom_nav_fragments_cliente.FragmentMisOrdenesC
import com.app.tiendavirtual.cliente.bottom_nav_fragments_cliente.FragmentTiendaC
import com.app.tiendavirtual.cliente.nav_fragments_cliente.FragmentInicioC
import com.app.tiendavirtual.cliente.nav_fragments_cliente.FragmentMiPerfilC
import com.app.tiendavirtual.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView

class MainActivityCliente : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

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
                Toast.makeText(applicationContext, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
            }
            R.id.op_tienda_c->{
                replaceFragment(FragmentTiendaC())
            }
            R.id.op_mis_ordenes_c->{
                replaceFragment(FragmentMisOrdenesC())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}