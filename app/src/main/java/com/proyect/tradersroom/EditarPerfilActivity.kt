package com.proyect.tradersroom

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.proyect.tradersroom.model.remote.UsuarioRemote
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import java.io.ByteArrayOutputStream
import java.io.File


class EditarPerfilActivity : AppCompatActivity() {

    var idUsuario : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        ib_foto.setOnClickListener {
            CropImage.startPickImageActivity(this)
        }

        val correo = consultarCorreo()

        buscarEnFirebase(correo)

        bt_guardar.setOnClickListener {
            actualizarDatos()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val storage = FirebaseStorage.getInstance()
        val imgRef = storage.reference.child("usuarios").child(idUsuario.toString())

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val imageuri = CropImage.getPickImageResultUri(this, data)

            //Recortar imagen
            CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize(480,480)
                .setAspectRatio(1, 1).start(this)
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK){
                val resultUri = result.uri
                val url = File(resultUri.path)

                Picasso.get().load(url).into(ib_foto)

                val thumb_bitmap = Compressor(this)
                    .setMaxWidth(480)
                    .setMaxHeight(480)
                    .setQuality(100)
                    .compressToBitmap(url)

                val byteArrayOutputStream = ByteArrayOutputStream()
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

                val thumb_byte: ByteArray = byteArrayOutputStream.toByteArray()

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Editar Foto")
                builder.setMessage("¿Desea modificar su foto de perfil?")

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    val uploadTask = imgRef.putBytes(thumb_byte)

                    val uriTask =
                        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            return@Continuation imgRef.downloadUrl
                        }).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result

                                //Toast.makeText(this, "URL: $downloadUri", Toast.LENGTH_SHORT).show()

                                actualizarFoto(downloadUri)
                            }
                        }
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    reloadActivity()
                }
                builder.show()
            }
        }
    }

    private fun goToPerfil() {
        finish()
        val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
    }

    private fun reloadActivity() {
        finish()
        startActivity(getIntent())
    }

    private fun actualizarDatos() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        val childUpdate = HashMap<String, Any>()
        childUpdate["nombre"] = et_nombre.text.toString()
        childUpdate["fecha"] = et_fecha.text.toString()
        childUpdate["telefono"] = et_telefono.text.toString()

        validarDatos(childUpdate, myRef)
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
                        et_fecha.setText(usuario?.fecha)
                        et_nombre.setText(usuario?.nombre)
                        et_telefono.setText(usuario?.telefono)
                        Picasso.get().load(usuario?.foto).into(ib_foto)
                        idUsuario = usuario?.id
                    }
                }
            }
        }

        myRef.addValueEventListener(postListener)
    }

    private fun actualizarFoto(downloadUri: Uri?) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")
        val childUpdate = HashMap<String, Any>()
        childUpdate["foto"] = downloadUri.toString()
        myRef.child(idUsuario!!).updateChildren(childUpdate)
        reloadActivity()
    }

    private fun validarDatos(childUpdate: HashMap<String, Any>, myRef: DatabaseReference) {
        when {
            childUpdate["nombre"] == "" -> { //Nombre Vacio
                et_nombre.error = "Ingrese su nombre"
                et_nombre.requestFocus()
            }
            childUpdate["fecha"] == "" -> { //Fecha Vacia
                et_fecha.error = "Ingrese su fecha de nacimiento"
                et_fecha.requestFocus()
            }
            childUpdate["telefono"] == "" -> { //Telefono Vacio
                et_telefono.error = "Ingrese su numero de telefono"
                et_telefono.requestFocus()
            }
            else -> {
                myRef.child(idUsuario!!).updateChildren(childUpdate)
                goToPerfil()
                Toast.makeText(this, "Información Actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

