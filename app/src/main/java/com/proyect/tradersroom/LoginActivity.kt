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
                et_email.error = "Por favor ingrese su correo"
            } else if (password.isEmpty()) { //CONTRASEÑA VACIA
                et_pass.error = "Por favor ingrese una contraseña"
            } else {

                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            goToMainActivity()
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException())
                            Toast.makeText(
                                this,
                                "User Authentication Failed: " + task.getException()?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            val exc = task.getException()?.localizedMessage

                            if (exc == "The email address is badly formatted.") {
                                et_email.setError("Formato invalido")
                                et_email.requestFocus()
                            } else if (exc == "There is no user record corresponding to this identifier. The user may have been deleted.") {
                                et_email.setError("Usuario no existe")
                                et_email.requestFocus()
                            } else if (exc == "The password is invalid or the user does not have a password") {
                                et_contrasena.setError("Contraseña incorrecta")
                                et_contrasena.requestFocus()
                            } else {
                                et_contrasena.setError("Contraseña incorrecta")
                                et_contrasena.requestFocus()
                            }
                        }
                        //if (code == "The email address is badly formatted.")
                        //val errorCode =  task.exception?.message

                        //identificarErrores(errorCode!!)
                    }
            }
        }

        bt_registro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    /*private fun identificarErrores(errorCode: String) {
        when (errorCode) {
            "ERROR_INVALID_CUSTOM_TOKEN" -> Toast.makeText(
                this,
                "The custom token format is incorrect. Please check the documentation.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_CUSTOM_TOKEN_MISMATCH" -> Toast.makeText(
                this,
                "The custom token corresponds to a different audience.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_INVALID_CREDENTIAL" -> Toast.makeText(
                this,
                "The supplied auth credential is malformed or has expired.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(
                    this,
                    "The email address is badly formatted.",
                    Toast.LENGTH_LONG
                ).show()
                et_correo.setError("The email address is badly formatted.")
                et_correo.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(
                    this,
                    "The password is invalid or the user does not have a password.",
                    Toast.LENGTH_LONG
                ).show()
                et_correo.setError("password is incorrect ")
                et_contrasena.requestFocus()
                et_contrasena.setText("")
            }
            "ERROR_USER_MISMATCH" -> Toast.makeText(
                this,
                "The supplied credentials do not correspond to the previously signed in user.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_REQUIRES_RECENT_LOGIN" -> Toast.makeText(
                this,
                "This operation is sensitive and requires recent authentication. Log in again before retrying this request.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> Toast.makeText(
                this,
                "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(
                    this,
                    "The email address is already in use by another account.   ",
                    Toast.LENGTH_LONG
                ).show()
                et_correo.setError("The email address is already in use by another account.")
                et_correo.requestFocus()
            }
            "ERROR_CREDENTIAL_ALREADY_IN_USE" -> Toast.makeText(
                this,
                "This credential is already associated with a different user account.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_USER_DISABLED" -> Toast.makeText(
                this,
                "The user account has been disabled by an administrator.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_USER_TOKEN_EXPIRED" -> Toast.makeText(
                this,
                "The user\\'s credential is no longer valid. The user must sign in again.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_USER_NOT_FOUND" -> Toast.makeText(
                this,
                "There is no user record corresponding to this identifier. The user may have been deleted.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_INVALID_USER_TOKEN" -> Toast.makeText(
                this,
                "The user\\'s credential is no longer valid. The user must sign in again.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_OPERATION_NOT_ALLOWED" -> Toast.makeText(
                this,
                "This operation is not allowed. You must enable this service in the console.",
                Toast.LENGTH_LONG
            ).show()
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(
                    this,
                    "The given password is invalid.",
                    Toast.LENGTH_LONG
                ).show()
                et_contrasena.setError("The password is invalid it must 6 characters at least")
                et_contrasena.requestFocus()
            }
        }
    }*/

    private fun goToMainActivity() {
        val intent = Intent(this, BottomNavigationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
