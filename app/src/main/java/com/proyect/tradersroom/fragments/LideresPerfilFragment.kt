package com.proyect.tradersroom.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.text.LineBreaker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mikhaellopez.circularimageview.CircularImageView
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.LiderRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_lideres_perfil.*


class LideresPerfilFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_lideres_perfil)

        val persona = intent.getSerializableExtra("personaId")

        ocultarEditText()

        consultarCorreo()

        buscarEnFirebase(persona.toString())

        imagenCircular()

    }

    private fun reloadActivity() {
        finish()
        startActivity(getIntent())
    }

    private fun imagenCircular() {
        val circularImageView = findViewById<CircularImageView>(R.id.circularImageView)
        circularImageView.apply {
            circleColorDirection = CircularImageView.GradientDirection.TOP_TO_BOTTOM
            borderWidth = 10f
            borderColorDirection = CircularImageView.GradientDirection.TOP_TO_BOTTOM
            shadowEnable = true
            shadowRadius = 7f
            shadowGravity = CircularImageView.ShadowGravity.CENTER
        }
    }

    private fun mostrarEditText() {
        et_nombre.visibility = View.VISIBLE
        et_roll.visibility = View.VISIBLE
        et_habilidades.visibility = View.VISIBLE
        et_profesion.visibility = View.VISIBLE
        et_ciudad.visibility = View.VISIBLE
        et_descripcion.visibility = View.VISIBLE

        bt_saveChanges2.visibility = View.VISIBLE
    }

    private fun ocultarEditText() {
        bt_saveChanges2.visibility = View.GONE

        et_nombre.visibility = View.GONE
        et_roll.visibility = View.GONE
        et_habilidades.visibility = View.GONE
        et_profesion.visibility = View.GONE
        et_ciudad.visibility = View.GONE
        et_descripcion.visibility = View.GONE
    }

    private fun ocultarTextView() {
        bt_config2.visibility = View.GONE
        tv_label_descripcion.visibility = View.GONE
        divider3.visibility = View.GONE
        divider4.visibility = View.GONE

        tv_nombre.visibility = View.GONE
        tv_roll.visibility = View.GONE
        tv_habilidades.visibility = View.GONE
        tv_profesion.visibility = View.GONE
        tv_ciudad.visibility = View.GONE
        tv_descripcion.visibility = View.GONE
    }

    private fun consultarCorreo(): String? {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email

        if (correo == "santiagocorrea54@gmail.com" || correo == "alejo.bravo9604@gmail.com")
            bt_config2.visibility = View.VISIBLE
        else
            bt_config2.visibility = View.GONE

        return correo
    }

    private fun buscarEnFirebase(idLider: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("lideres")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val lider = datasnapshot.getValue(LiderRemote::class.java)

                    if (lider?.id == idLider){
                        tv_nombre.setText(lider?.nombre)
                        tv_roll.setText(lider?.roll)
                        tv_habilidades.setText(lider?.rango)
                        tv_profesion.setText(lider?.profesion)
                        tv_ciudad.setText(lider?.ciudad)
                        tv_descripcion.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD)
                        tv_descripcion.setText(lider?.descripcion)
                        Picasso.get().load(lider?.foto2).into(iv_fondo)
                        Picasso.get().load(lider?.foto).into(circularImageView)

                        bt_facebook.setOnClickListener {
                            val urlPage = lider?.face.toString()
                            goToPage(urlPage)
                        }

                        bt_instagram.setOnClickListener {
                            val urlPage = lider?.insta.toString()
                            goToInstagram(urlPage)
                        }

                        bt_wpp.setOnClickListener {
                            val urlPage = lider?.wpp.toString()
                            goToPage(urlPage)
                        }

                        bt_config2.setOnClickListener {
                            ocultarTextView()
                            mostrarEditText()
                            cargarDatosEditText(lider)
                            bt_saveChanges2.setOnClickListener {
                                actualizarDatos(idLider)
                            }
                        }

                    }
                }
            }
        }

        myRef.addValueEventListener(postListener)
    }

    private fun goToInstagram(urlPage: String) {
        val uri = Uri.parse("http://instagram.com/_u/$urlPage")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.instagram.android")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //No encontró la aplicación, abre la versión web.
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$urlPage")))
        }
    }

    private fun goToPage(urlPage: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)))
    }

    private fun cargarDatosEditText(lider: LiderRemote?) {
        et_nombre.setText(lider?.nombre)
        et_roll.setText(lider?.roll)
        et_habilidades.setText(lider?.rango)
        et_profesion.setText(lider?.profesion)
        et_ciudad.setText(lider?.ciudad)
        et_descripcion.setText(lider?.descripcion)
    }

    private fun actualizarDatos(idLider: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("lideres")

        val childUpdate = HashMap<String, Any>()
        childUpdate["nombre"] = et_nombre.text.toString()
        childUpdate["roll"] = et_roll.text.toString()
        childUpdate["rango"] = et_habilidades.text.toString()
        childUpdate["profesion"] = et_profesion.text.toString()
        childUpdate["ciudad"] = et_ciudad.text.toString()
        childUpdate["descripcion"] = et_descripcion.text.toString()

        validarDatos(childUpdate, myRef, idLider)
    }

    private fun validarDatos(childUpdate: HashMap<String, Any>, myRef: DatabaseReference, idLider: String?) {
        when {
            childUpdate["nombre"] == "" -> { //Nombre Vacio
                et_nombre.error = "Ingrese el nombre"
                et_nombre.requestFocus()
            }
            childUpdate["roll"] == "" -> { //Roll Vacio
                et_roll.error = "Ingrese el roll"
                et_roll.requestFocus()
            }
            childUpdate["rango"] == "" -> { //Habilidades Vacias
                et_habilidades.error = "Ingrese las habilidades"
                et_habilidades.requestFocus()
            }
            childUpdate["profesion"] == "" -> { //Profesion Vacia
                et_profesion.error = "Ingrese la profesion"
                et_profesion.requestFocus()
            }
            childUpdate["ciudad"] == "" -> { //Ciudad Vacia
                et_ciudad.error = "Ingrese la ciudad"
                et_ciudad.requestFocus()
            }
            childUpdate["descripcion"] == "" -> { //Descripcion Vacia
                et_descripcion.error = "Ingrese una descripcion"
                et_descripcion.requestFocus()
            }
            else -> {
                myRef.child(idLider!!).updateChildren(childUpdate)
                reloadActivity()
                Toast.makeText(this, "Información Actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}