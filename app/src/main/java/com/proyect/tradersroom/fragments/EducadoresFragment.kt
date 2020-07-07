package com.proyect.tradersroom.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.proyect.tradersroom.R
import kotlinx.android.synthetic.main.fragment_educadores.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EducadoresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_educadores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*bt_educadores.setOnClickListener {
            findNavController().navigate(R.id.action_EducadoresFragment_to_LideresFragment)
        }*/
    }
}