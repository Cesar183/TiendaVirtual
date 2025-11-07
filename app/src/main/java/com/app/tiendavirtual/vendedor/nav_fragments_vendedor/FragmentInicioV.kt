package com.app.tiendavirtual.vendedor.nav_fragments_vendedor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.tiendavirtual.R
import com.app.tiendavirtual.databinding.FragmentInicioVBinding
import com.app.tiendavirtual.vendedor.bottom_nav_fragments_vendedor.FragmentMisProductosV
import com.app.tiendavirtual.vendedor.bottom_nav_fragments_vendedor.FragmentOrdenesV
import com.app.tiendavirtual.vendedor.productos.AgregarProductoActivity

class FragmentInicioV : Fragment() {

    private lateinit var binding: FragmentInicioVBinding
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioVBinding.inflate(inflater,container,false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_mis_productos_v->{
                    replaceFragment(FragmentMisProductosV())
                }
                R.id.op_mis_ordenes_v->{
                    replaceFragment(FragmentOrdenesV())
                }
            }
            true
        }
        replaceFragment(FragmentMisProductosV())
        binding.bottomNavigation.selectedItemId = R.id.op_mis_productos_v

        binding.addFab.setOnClickListener {
            startActivity(Intent(context, AgregarProductoActivity::class.java))
        }
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }
}