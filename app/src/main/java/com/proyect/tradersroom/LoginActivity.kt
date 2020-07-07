package com.proyect.tradersroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //BOTON REGISTRO
        button.setOnClickListener {
            val intent: Intent = Intent(this, RegistroActivity::class.java)
            startActivityForResult(intent, 1234)
        }

        //BOTON DE LOGIN
        button3.setOnClickListener {

            // CORREO Y CONTRASEÑA INGRESADOS EN LOGIN
            val loginCorreo = et_correo2.text.toString()
            val loginContrasena = et_pass.text.toString()


            val registroCorreo = intent.getStringExtra("correo")
            val registroContrasena = intent.getStringExtra("pass")

            // CORREO VACIO
            if (loginCorreo.isEmpty()) {
                et_correo2.error = "Por favor ingrese el correo"
            }

            // CONTRASEÑA VACIA
            else if (loginContrasena.isEmpty()) {
                et_pass.error = "Por favor ingrese la contraseña"
            }

            // CORREO Y CONTRASEÑA VACIOS
            else if (loginCorreo.isEmpty() && loginContrasena.isEmpty()) {
                et_correo2.error = "Por favor ingrese el correo"
                et_pass.error = "Por favor ingrese la contraseña"
            }

            // CORREO Y CONTRASEÑA VERIFICADOS
            else if (loginCorreo == registroCorreo && loginContrasena == registroContrasena) {

                // ENVIAR DATOS AL MAIN
                /*val intent = Intent(this, BottomNavigationActivity::class.java)
                //val intent = Intent(this, MainActivity::class.java)
                /intent.putExtra("correo", et_correo2.text.toString())
                intent.putExtra("pass", et_pass.text.toString())
                startActivity(intent)*/

                val intent = Intent(this, BottomNavigationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent);


            }

            //CORREO INCORRECTO
            else if (loginCorreo != registroCorreo && loginContrasena == registroContrasena) {
                et_correo2.error = "El correo es incorrecto"
            }

            //CONTRASEÑA INCORRECTA
            else if (loginCorreo == registroCorreo && loginContrasena != registroContrasena) {
                et_pass.error = "La contraseña es incorrecta"
            }

            //CORREO Y CONTRASEÑA INCORRECTAS
            else {
                et_correo2.error = "El correo es incorrecto"
                et_pass.error = "La contraseña es incorrecta"
            }
        }
    }

    //ACTIVIDAD PARA RECIBIR DATOS DEL REGISTRO
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            val registroCorreo = data?.extras?.getString("correo")
            val registroContrasena = data?.extras?.getString("contrasena")

            et_correo2.setText(data?.extras?.getString("correo"))
            et_pass.setText(data?.extras?.getString("contrasena"))

            // SI NO SE RECIBIO NADA SE DEVUELVE AL REGISTRO
            if (registroCorreo?.isEmpty()!! || registroContrasena?.isEmpty()!!) {
                val intent = Intent(this, RegistroActivity::class.java)
                startActivity(intent)
            }

            //Toast.makeText(this, "correo: $registroCorreo, contrasena $registroContrasena", Toast.LENGTH_SHORT).show()

            //BOTON INICIAR SESION
            button3.setOnClickListener {

                // CORREO Y CONTRASEÑA INGRESADOS EN LOGIN
                val loginCorreo = et_correo2.text.toString()
                val loginContrasena = et_pass.text.toString()


                // CORREO VACIO
                if (loginCorreo.isEmpty()) {
                    et_correo2.error = "Por favor ingrese el correo"
                }

                // CONTRASEÑA VACIA
                else if (loginContrasena.isEmpty()) {
                    et_pass.error = "Por favor ingrese la contraseña"
                }

                // CORREO Y CONTRASEÑA VACIOS
                else if (loginCorreo.isEmpty() && loginContrasena.isEmpty()) {
                    et_correo2.error = "Por favor ingrese el correo y la contraseña"
                }

                // CORREO Y CONTRASEÑA VERIFICADO
                else if (loginCorreo == registroCorreo && loginContrasena == registroContrasena) {

                    // ENVIAR DATOS AL MAIN
                    /*val intent = Intent(this, BottomNavigationActivity::class.java)
                    //val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("correo", et_correo2.text.toString())
                    intent.putExtra("pass", et_pass.text.toString())
                    startActivity(intent)*/

                    val intent = Intent(this, BottomNavigationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent);
                }

                //CORREO INCORRECTO
                else if (loginCorreo != registroCorreo && loginContrasena == registroContrasena) {
                    et_correo2.error = "El correo es incorrecto"
                }

                //CONTRASEÑA INCORRECTA
                else if (loginCorreo == registroCorreo && loginContrasena != registroContrasena) {
                    et_pass.error = "La contraseña es incorrecta"
                }

                //CORREO Y CONTRASEÑA INCORRECTAS
                else {
                    et_correo2.error = "El correo es incorrecto"
                    et_pass.error = "La contraseña es incorrecta"
                }
            }
        }
    }
}