package com.proyect.tradersroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*

class LoginActivity : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        bt_ingresar.setOnClickListener {

            val email = et_email.text.toString()
            val password = et_pass.text.toString()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, BottomNavigationActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.getException())
                        Toast.makeText(this, "User Authentication Failed: " + task.getException()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        bt_registro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}
