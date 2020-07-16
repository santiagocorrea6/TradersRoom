package com.proyect.tradersroom

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyect.tradersroom.model.remote.UsuarioRemote
import kotlinx.android.synthetic.main.activity_registro.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class RegistroActivity : AppCompatActivity() {

    private lateinit var fecha: String
    private var cal = Calendar.getInstance()
    var flagRegistro = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)



        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val format = "MM/dd/yyyy"
                val simpleDateFormat = SimpleDateFormat(format, Locale.US)
                fecha = simpleDateFormat.format(cal.time).toString()
                tv_fecha_nacimiento.text = fecha
            }
        }

        ib_calendario.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //AQUI EMPIEZA FIREBASE
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        bt_registrar.setOnClickListener {

            val nombre = et_nombre.text.toString()
            val telefono = et_telefono.text.toString()
            val correo = et_correo.text.toString()
            val contrasena = et_contrasena.text.toString()
            val rep_contrasena = et_contrasena2.text.toString()
            var roll = sp_roll.selectedItem.toString()

            val passLength: Int = stringLengthFunc("$contrasena")
            tv_resultado.text = "$passLength"

            if (nombre.isEmpty()) { //NOMBRE VACIO
                et_nombre.error = "Por favor ingrese su nombre"
            } else if (telefono.isEmpty()) { //TELEFONO VACIO
                et_telefono.error = "Por favor ingrese su numero telefonico"
            } else if (correo.isEmpty()) { //CORREO VACIO
                et_correo.error = "Por favor ingrese su correo"
            } else if (!validarEmail(correo)) {
                et_correo.setError("Email no válido")
            } else if (contrasena.isEmpty()) { //CONTRASEÑA VACIA
                et_contrasena.error = "Por favor ingrese una contraseña"
            } else if (passLength < 6) { //CONTRASEÑA DE 6 DIGITOS
                et_contrasena.error = "La contraseña debe tener minimo 6 caracteres"
            } else if (rep_contrasena.isEmpty()) { //CONTRASEÑA 2 VACIA
                et_contrasena2.error = "Por favor repita su contraseña"
            } else if (contrasena != rep_contrasena) { //CONTRASEÑAS DIFERENTES
                et_contrasena2.error = "Las contraseñas no coinciden"
            } else if (tv_fecha_nacimiento.text.toString().isEmpty()) { // FECHA VACIA
                tv_fecha_nacimiento.error = "Por favor ingrese su fecha de nacimiento"
                tv_resultado.text = "Por favor ingrese su fecha de nacimiento"
            } else if (tv_fecha_nacimiento.text.toString() == "MM/dd/yyyy") { //FECHA INCORRECTA
                tv_fecha_nacimiento.error = "Por favor ingrese su fecha de nacimiento"
                tv_resultado.text = "Por favor ingrese su fecha de nacimiento"
            }

            else {
                //if (contrasena == rep_contrasena && contrasena.isNotEmpty() && rep_contrasena.isNotEmpty()) {
                mAuth.createUserWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {

                            crearUsuarioEnFirebase(nombre, telefono, correo, roll)
                            Toast.makeText(
                                this, "Registro exitoso",
                                Toast.LENGTH_SHORT
                            ).show()
                            FirebaseAuth.getInstance().signOut()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()

                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException())
                            Toast.makeText(
                                this,
                                "User Authentication Failed: " + task.getException()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }


        }
    }

    private fun crearUsuarioEnFirebase(
        nombre: String,
        telefono: String,
        correo: String,
        roll: String
    ) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("usuarios")

        val id = myRef.push().key

        val usuario = UsuarioRemote(
            id,
            nombre,
            telefono,
            correo,
            "hoy",
            roll,
            "https://url2.cl/cjv4I"
        )

        myRef.child(id!!).setValue(usuario)
    }

    //VERIFICA LA LONGITUD DE UN STRING
    val stringLengthFunc: (String) -> Int = { input ->
        input.length
    }

    //Validar correo
    private fun validarEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}