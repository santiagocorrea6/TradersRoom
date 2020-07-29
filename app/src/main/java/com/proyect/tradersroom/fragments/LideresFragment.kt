package com.proyect.tradersroom.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.LiderRemote
import com.proyect.tradersroom.ui.EducadoresRVAdapter
import com.proyect.tradersroom.ui.LideresRVAdapter
import kotlinx.android.synthetic.main.fragment_lideres.*


class LideresFragment : Fragment(), LideresRVAdapter.OnLiderClickListener {

    private val lideresList: MutableList<LiderRemote> = mutableListOf()
    private lateinit var lideresAdapter : LideresRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lideres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarLideres()

        rv_lideres.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        rv_lideres.setHasFixedSize(true) //todos del mismo tamaño

        lideresAdapter = LideresRVAdapter(lideresList as ArrayList<LiderRemote>, this)
        rv_lideres.adapter = lideresAdapter

    }

    private fun cargarLideres() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("lideres")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val lider = datasnapshot.getValue(LiderRemote::class.java)
                    lideresList.add(lider!!)
                }

                lideresAdapter.notifyDataSetChanged()
            }
        }
        myRef.addValueEventListener(postListener)
    }

    override fun onItemClick(id: String) {
        val intent = Intent(requireContext(), LideresPerfilFragment::class.java)
        intent.putExtra("personaId", id)
        startActivity(intent)
    }
}
