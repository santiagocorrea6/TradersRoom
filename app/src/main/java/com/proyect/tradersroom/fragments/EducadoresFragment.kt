package com.proyect.tradersroom.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.EducadorRemote
import com.proyect.tradersroom.ui.EducadoresRVAdapter
import kotlinx.android.synthetic.main.educadores_item.*
import kotlinx.android.synthetic.main.fragment_educadores.*
import kotlinx.android.synthetic.main.fragment_home.*


class EducadoresFragment : Fragment() {

    private val educadoresList: MutableList<EducadorRemote> = mutableListOf()
    private lateinit var educadoresAdapter : EducadoresRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_educadores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarEducadores()

        rv_educadores.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
           false
        )

        rv_educadores.setHasFixedSize(true) //todos del mismo tamaño

        educadoresAdapter = EducadoresRVAdapter(educadoresList as ArrayList<EducadorRemote>)
        rv_educadores.adapter = educadoresAdapter

    }

    private fun cargarEducadores() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("educadores")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val educador = datasnapshot.getValue(EducadorRemote::class.java)
                    educadoresList.add(educador!!)
                }

                educadoresAdapter.notifyDataSetChanged()
            }
        }
        myRef.addValueEventListener(postListener)
    }
}