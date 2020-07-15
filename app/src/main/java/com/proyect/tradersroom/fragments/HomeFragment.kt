package com.proyect.tradersroom.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.UsuarioRemote
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email

        Toast.makeText(requireContext(), "Bienvenido $correo", Toast.LENGTH_SHORT).show()

        buscarEnFirebase(correo)

    }

    private fun buscarEnFirebase(correo: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        val postListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val usuario = datasnapshot.getValue(UsuarioRemote::class.java)

                    if (usuario?.correo == correo){
                        tv_hola.text = "Bienvenido ${usuario?.nombre}"
                        //Toast.makeText(requireContext(), "Bienvenido ${usuario?.nombre}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        myRef.addValueEventListener(postListener)

    }
}