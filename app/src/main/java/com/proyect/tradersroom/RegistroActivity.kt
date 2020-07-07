package com.proyect.tradersroom

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registro.*
import java.text.SimpleDateFormat
import java.util.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var fecha: String
    private var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  //Ciclo de vida
        setContentView(R.layout.activity_registro) //Implementa contenido de vista
        Log.d("OnCreate", "ok")

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

        // BOTON DEL CALENDARIO
        ib_calendario.setOnClickListener {
            // fun onCalendarioButtonClicked(view: View) {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
            //    }
        }

        // BOTON GUARDAR REGISTRO
        bt_guardar.setOnClickListener {
            Log.d("ButtonClicked", "true")

            // DECLARACION DE LAS VARIABLES
            var pasatiempos = ""
            val nombre = et_nombre.text.toString()
            val cedula = et_cedula.text.toString()
            val correo = et_correo.text.toString()
            val contrasena = et_contrasena.text.toString()
            val rep_contrasena = et_contrasena2.text.toString()
            //val genero = if (rb_masculino.isChecked) "Masculino" else "Femenino"
            var ciudadNacimiento = sp_ciudad_nacimiento.selectedItem.toString()

            val passLength: Int = stringLengthFunc("$contrasena")
            tv_resultado.text = "$passLength"

            // VERIFICACION DE LOS PASATIEMPOS
            //if (ch_musica.isChecked) pasatiempos = "$pasatiempos \n cine"
            //if (ch_deportes.isChecked) pasatiempos = "$pasatiempos \nps4"
            //if (ch_netflix.isChecked) pasatiempos = "$pasatiempos \nseries"

            // VERIFICACION DE LOS CAMPOS LLENADOS
            if (nombre.isEmpty()) { //NOMBRE VACIO
                et_nombre.error = "Por favor ingrese su nombre"
            } else if (cedula.isEmpty()) { //CEDULA VACIA
                et_cedula.error = "Por favor ingrese su numero de cedula"
            } else if (correo.isEmpty()) { //CORREO VACIO
                et_correo.error = "Por favor ingrese su correo"
            } else if (contrasena.isEmpty()) { //CONTRASEÑA VACIA
                et_contrasena.error = "Por favor ingrese una contraseña"
            } else if (passLength < 6) { //CONTRASEÑA DE 6 DIGITOS
                et_contrasena.error = "La contraseña debe tener minimo 6 caracteres"
            } else if (rep_contrasena.isEmpty()) { //CONTRASEÑA 2 VACIA
                et_contrasena2.error = "Por favor repita su contraseña"
            } else if (contrasena != rep_contrasena) { //CONTRASEÑAS DIFERENTES
                et_contrasena2.error = "Las contraseñas no coinciden"
            }

            // CONTRASEÑAS IGUALES Y TODOS LOS CAMPOS LLENOS
            else if (contrasena == rep_contrasena && contrasena.isNotEmpty() && rep_contrasena.isNotEmpty()) {
                //tv_resultado.text = "DATOS PERSONALES \n Nombre: $nombre \n Cedula: $cedula \n Correo: $correo \n Genero: $genero \n Ciudad: $ciudadNacimiento"

                val intent = Intent()
                intent.putExtra("correo", et_correo.text.toString())
                intent.putExtra("contrasena", et_contrasena.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            // FECHA VACIA
            else if (fecha.isEmpty()) {
                tv_fecha_nacimiento.error = "Por favor ingrese su fecha de nacimiento"
            }

            // ERROR GENERAL EN EL REGISTRO
            else {
                tv_resultado.text = "Error en el registro"
            }
        }
    }

    // VERIFICA LA LONGITUD DE UN STRING
    val stringLengthFunc: (String) -> Int = { input ->
        input.length
    }
}