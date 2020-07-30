package com.proyect.tradersroom.fragments

//https://github.com/lopspower/CircularImageView
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
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
import com.proyect.tradersroom.model.remote.EducadorRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_personas.*
import kotlinx.android.synthetic.main.fragment_personas.et_nombre


class EducadoresPerfilFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_personas)

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

        bt_saveChanges.visibility = View.VISIBLE
    }

    private fun ocultarEditText() {
        bt_saveChanges.visibility = View.GONE

        et_nombre.visibility = View.GONE
        et_roll.visibility = View.GONE
        et_habilidades.visibility = View.GONE
        et_profesion.visibility = View.GONE
        et_ciudad.visibility = View.GONE
        et_descripcion.visibility = View.GONE
    }

    private fun ocultarTextView() {
        bt_config.visibility = View.GONE
        textView4.visibility = View.GONE
        divider.visibility = View.GONE
        divider2.visibility = View.GONE

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
            bt_config.visibility = View.VISIBLE
        else
            bt_config.visibility = View.GONE

        return correo
    }

    private fun buscarEnFirebase(idEducador: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("educadores")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val educador = datasnapshot.getValue(EducadorRemote::class.java)

                    if (educador?.id == idEducador){
                        tv_nombre.setText(educador?.nombre)
                        tv_roll.setText(educador?.roll)
                        tv_habilidades.setText(educador?.habilidades)
                        tv_profesion.setText(educador?.profesion)
                        tv_ciudad.setText(educador?.ciudad)
                        tv_descripcion.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD)
                        tv_descripcion.setText(educador?.descripcion)
                        Picasso.get().load(educador?.foto).into(circularImageView)
                        Picasso.get().load(educador?.foto2).into(iv_fondo)


                        bt_config.setOnClickListener {
                            ocultarTextView()
                            mostrarEditText()
                            cargarDatosEditText(educador)
                            bt_saveChanges.setOnClickListener {
                                actualizarDatos(idEducador)
                            }
                        }

                    }
                }
            }
        }

        myRef.addValueEventListener(postListener)
    }

    private fun cargarDatosEditText(educador: EducadorRemote?) {
        et_nombre.setText(educador?.nombre)
        et_roll.setText(educador?.roll)
        et_habilidades.setText(educador?.habilidades)
        et_profesion.setText(educador?.profesion)
        et_ciudad.setText(educador?.ciudad)
        et_descripcion.setText(educador?.descripcion)
    }

    private fun actualizarDatos(idEducador: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("educadores")

        val childUpdate = HashMap<String, Any>()
        childUpdate["nombre"] = et_nombre.text.toString()
        childUpdate["roll"] = et_roll.text.toString()
        childUpdate["habilidades"] = et_habilidades.text.toString()
        childUpdate["profesion"] = et_profesion.text.toString()
        childUpdate["ciudad"] = et_ciudad.text.toString()
        childUpdate["descripcion"] = et_descripcion.text.toString()

        validarDatos(childUpdate, myRef, idEducador)
    }

    private fun validarDatos(childUpdate: HashMap<String, Any>, myRef: DatabaseReference, idEducador: String?) {
        when {
            childUpdate["nombre"] == "" -> { //Nombre Vacio
                et_nombre.error = "Ingrese el nombre"
                et_nombre.requestFocus()
            }
            childUpdate["roll"] == "" -> { //Roll Vacio
                et_roll.error = "Ingrese el roll"
                et_roll.requestFocus()
            }
            childUpdate["habilidades"] == "" -> { //Habilidades Vacias
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
                myRef.child(idEducador!!).updateChildren(childUpdate)
                reloadActivity()
                Toast.makeText(this, "Información Actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}