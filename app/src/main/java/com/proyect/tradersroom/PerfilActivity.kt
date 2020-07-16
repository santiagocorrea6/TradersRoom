package com.proyect.tradersroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proyect.tradersroom.model.remote.UsuarioRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.educadores_item.view.*

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email

        Toast.makeText(this, "Bienvenido $correo", Toast.LENGTH_SHORT).show()

        buscarEnFirebase(correo)
    }

    private fun buscarEnFirebase(correo: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val usuario = datasnapshot.getValue(UsuarioRemote::class.java)

                    if (usuario?.correo == correo){
                        tv_correo.text = usuario?.correo
                        tv_fecha.text = usuario?.fecha
                        tv_nombre.text = usuario?.nombre
                        tv_roll.text = usuario?.roll
                        tv_telefono.text = usuario?.telefono
                        Picasso.get().load(usuario?.foto).into(iv_perfil)
                    }
                }
            }
        }

        myRef.addValueEventListener(postListener)
    }
}