package com.proyect.tradersroom

//import android.support.v4.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        val timer = Timer()
        timer.schedule(
            timerTask {
                val user = mAuth.currentUser
                if (user != null) {
                    goToMainActivity()
                } else
                    goToLoginActivity()
            }, 1000
        )
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@SplashActivity, BottomNavigationActivity::class.java))
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}