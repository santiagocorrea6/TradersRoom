package com.proyect.tradersroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularimageview.CircularImageView
import com.proyect.tradersroom.model.remote.UsuarioRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        imagenCircular()

        val correo = consultarCorreo()
        buscarEnFirebase(correo)

        ib_config.setOnClickListener {
            goToEditar()
        }

        bt_cerrar.setOnClickListener {
            goToLogin()
        }
    }

    private fun imagenCircular() {
        val circularImageView = findViewById<CircularImageView>(R.id.iv_perfil)
        circularImageView.apply {
            circleColorDirection = CircularImageView.GradientDirection.TOP_TO_BOTTOM
            borderWidth = 10f
            borderColorDirection = CircularImageView.GradientDirection.TOP_TO_BOTTOM
            shadowEnable = true
            shadowRadius = 7f
            shadowGravity = CircularImageView.ShadowGravity.CENTER
        }
    }

    private fun goToLogin() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun goToEditar() {
        finish()
        val intent = Intent(this, EditarPerfilActivity::class.java)
        startActivity(intent)
    }

    private fun consultarCorreo(): String? {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email
        return correo
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
                        tv_correo.setText(usuario?.correo)
                        tv_fecha.setText(usuario?.fecha)
                        tv_nombre.setText(usuario?.nombre)
                        tv_roll.setText(usuario?.roll)
                        tv_telefono.setText(usuario?.telefono)
                        Picasso.get().load(usuario?.foto).into(iv_perfil)
                    }
                }
            }
        }

        myRef.addValueEventListener(postListener)
    }
}