package com.app.tiendavirtual.localizacion

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.app.tiendavirtual.R
import kotlinx.coroutines.launch

class FragmentLocation : Fragment() {

    private val locationService: LocationService = LocationService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLocation = view.findViewById<TextView>(R.id.tvLocation)
        val btnLocation = view.findViewById<Button>(R.id.btnLocation)

        btnLocation.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = locationService.getUserLocation(requireActivity())
                if (result != null) {
                    tvLocation.text = "latitud ${result.latitude} y longitud ${result.longitude}"
                }
            }
        }
    }
}