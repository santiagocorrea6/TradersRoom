package com.proyect.tradersroom

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.proyect.tradersroom.model.remote.BitacoraRemote
import com.proyect.tradersroom.model.remote.UsuarioRemote
import com.proyect.tradersroom.ui.BitacorasRVAdapter
import kotlinx.android.synthetic.main.activity_resumen.*
import kotlin.collections.ArrayList

class ResumenActivity : AppCompatActivity() {
    private val bitacorasList: MutableList<BitacoraRemote> = mutableListOf()
    private lateinit var bitacorasAdapter : BitacorasRVAdapter

    var maxId : Int = 0
    var bitacoraId = "hola"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)
        tv_capitalActual.text = "$10"
        tv_capitalInicial.text = "$10"

        cargarBitacora()

        bt_fila.setOnClickListener { alertDialogEliminarFila() }

        bt_tabla.setOnClickListener {alertDialogLimpiarTabla() }

        rv_bitacoras.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        rv_bitacoras.setHasFixedSize(true) //todos del mismo tamaño

        bitacorasAdapter = BitacorasRVAdapter(bitacorasList as ArrayList<BitacoraRemote>)
        rv_bitacoras.adapter = bitacorasAdapter

    }

    private fun alertDialogEliminarFila() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Fila")
        builder.setMessage("¿Desea eliminar el ultimo registro?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            val correo = consultarCorreo()

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("usuarios")

            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (datasnapshot: DataSnapshot in dataSnapshot.children) {

                        val usuario = datasnapshot.getValue(UsuarioRemote::class.java)

                        if (usuario?.correo == correo) {

                            borrarFila(database, usuario)

                            reloadActivity()
                        }
                    }
                }
            }
            myRef.addValueEventListener(postListener)
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which -> }
        builder.show()
    }

    private fun alertDialogLimpiarTabla() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Limpiar Tabla")
        builder.setMessage("¿Desea eliminar todos los registros?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            val correo = consultarCorreo()

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("usuarios")

            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (datasnapshot: DataSnapshot in dataSnapshot.children) {

                        val usuario = datasnapshot.getValue(UsuarioRemote::class.java)

                        if (usuario?.correo == correo) {

                            borrarBitacora(database, usuario)

                            reloadActivity()
                        }
                    }
                }
            }
            myRef.addValueEventListener(postListener)

            Toast.makeText(applicationContext,"Registros Eliminados", Toast.LENGTH_SHORT).show()

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which -> }
        builder.show()
    }

    private fun reloadActivity() {
        finish()
        startActivity(getIntent())
    }

    private fun cargarBitacora() {
        val correo = consultarUsuario()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")



        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in dataSnapshot.children) {

                    val usuario = datasnapshot.getValue(UsuarioRemote::class.java)

                    if (usuario?.correo == correo) {

                        val myRef2 = database.getReference("bitacora").child("${usuario?.id}")

                        val postListener = object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                var capitalActual : String = ""
                                var capitalInicial : String = ""

                                for(datasnapshot: DataSnapshot in snapshot.children){
                                    val bitacora = datasnapshot.getValue(BitacoraRemote::class.java)
                                    bitacorasList.add(bitacora!!)

                                    capitalActual = bitacora.capitalInicial
                                    //tv_capitalActual.text = "$$capitalActual"
                                    //tv_capitalInicial.text = "$$capitalInicial"
                                    if (bitacora.id == "0") {
                                        capitalInicial = bitacora.capitalInicial
                                        //tv_capitalInicial.text = "$$capitalInicial"
                                    }

                                    writeInTextView(capitalActual, capitalInicial)
                                }
                                bitacorasAdapter.notifyDataSetChanged()
                            }
                        }
                        myRef2.addValueEventListener(postListener)
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ResumenActivity, "Error al cargar", Toast.LENGTH_SHORT).show()
            }
        }
        myRef.addValueEventListener(postListener)
    }

    @SuppressLint("SetTextI18n")
    private fun writeInTextView(capitalActual: String, capitalInicial: String) {
        tv_capitalInicial.setText("$$capitalInicial")
        tv_capitalActual.setText("$$capitalActual")
    }

    private fun consultarUsuario(): String? {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email
        return correo
    }

    private fun consultarCorreo(): String? {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email
        return correo
    }

    private fun borrarBitacora(database: FirebaseDatabase,usuario: UsuarioRemote?) {
        val myRef2: DatabaseReference = database.getReference("bitacora").child("${usuario?.id}")
        val bitacora = BitacoraRemote(
            "0",
            "0",
            "0",
            "0",
            "0",
            "0",
            "0",
            "0",
            "0"
        )

        myRef2.setValue("0")
        myRef2.child("0").setValue(bitacora)
    }

    private fun borrarFila(database: FirebaseDatabase,usuario: UsuarioRemote?) {
        val bitacoraRef: DatabaseReference = database.getReference("bitacora").child("${usuario?.id}")

        val postListenerId = object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                maxId = (dataSnapshot.childrenCount.toInt())
            }
        }

        bitacoraRef.addValueEventListener(postListenerId)

        Toast.makeText(this, "MAX: $maxId", Toast.LENGTH_SHORT).show()

        if (maxId <= 1)
            Toast.makeText(this@ResumenActivity, "Registro Vacio", Toast.LENGTH_SHORT).show()
        else {
            bitacoraRef.child("${maxId}").removeValue()
        }
    }
}