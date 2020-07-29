package com.proyect.tradersroom.fragments

//https://github.com/lopspower/CircularImageView
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularimageview.CircularImageView
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.EducadorRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_personas.*


class EducadoresPerfilFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_personas)

        val persona = intent.getSerializableExtra("personaId")

        //Toast.makeText(this, "${persona}", Toast.LENGTH_SHORT).show()

        buscarEnFirebase(persona.toString())

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

    private fun buscarEnFirebase(idEducador: String?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("educadores")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val educador = datasnapshot.getValue(EducadorRemote::class.java)

                    if (educador?.id == idEducador){
                        tv_nombre.setText(educador?.nombre)
                        tv_roll.setText(educador?.roll)
                        tv_habilidades.setText(educador?.habilidades)
                        tv_profesion.setText(educador?.profesion)
                        tv_ciudad.setText(educador?.ciudad)
                        tv_descripcion.setText(educador?.descripcion)
                        Picasso.get().load(educador?.foto).into(circularImageView)
                    }
                }
            }
        }

        myRef.addValueEventListener(postListener)
    }
}