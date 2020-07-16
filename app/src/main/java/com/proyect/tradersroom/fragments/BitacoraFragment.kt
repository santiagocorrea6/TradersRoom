package com.proyect.tradersroom.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.proyect.tradersroom.R
import kotlinx.android.synthetic.main.fragment_bitacora.*

class BitacoraFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bitacora, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_resumen.setOnClickListener {
            findNavController().navigate(R.id.action_nav_bitacora_to_resumenFragment)
        }
    }


}