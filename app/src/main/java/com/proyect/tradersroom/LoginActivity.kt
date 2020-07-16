package com.proyect.tradersroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*

class LoginActivity : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bt_ingresar.setOnClickListener {

            val email = et_email.text.toString()
            val password = et_pass.text.toString()

            if (email.isEmpty()) { //CORREO VACIO
                emailVoid()
            } else if (password.isEmpty()) { //CONTRASEÑA VACIA
                passVoid()
            } else {

                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            goToMainActivity()
                        } else {
                            Toast.makeText(this, task.getException()?.message, Toast.LENGTH_SHORT).show()
                            val exc = task.getException()?.localizedMessage
                            verificarCodigosError(exc)
                        }
                    }
            }
        }

        bt_registro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun passVoid() {
        et_pass.error = "Ingrese la contraseña"
        et_pass.requestFocus()
    }

    private fun emailVoid() {
        et_email.error = "Ingrese el correo"
        et_email.requestFocus()
    }

    private fun verificarCodigosError(exc: String?) {
        if (exc == "The email address is badly formatted.") {
            et_email.setError("Formato invalido")
            et_email.requestFocus()
        } else if (exc == "There is no user record corresponding to this identifier. The user may have been deleted.") {
            et_email.setError("Usuario no existe")
            et_email.requestFocus()
        } else if (exc == "The password is invalid or the user does not have a password.") {
            et_pass.setError("Contraseña incorrecta")
            et_pass.requestFocus()
        } else if (exc == "The email address is already in use by another account.") {
            et_email.setError("Usuario ya registrado")
        } else if (exc == "The user account has been disabled by an administrator.") {
            et_email.setError("Cuenta desabilitada")
        } else if (exc == "There is no user record corresponding to this identifier. The user may have been deleted.") {
            et_email.setError("Usuario no existe")
        } else if (exc == "The given password is invalid.") {
            et_email.setError("Contraseña incorrecta")
        } else if (exc == "The password is invalid it must 6 characters at least.") {
            et_email.setError("Minimo 6 caracteres")
        } else {
            et_pass.setError("Contraseña incorrecta")
            et_pass.setError("Contraseña incorrecta")
            et_pass.requestFocus()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, BottomNavigationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
