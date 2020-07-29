package com.proyect.tradersroom

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class BottomNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_educadores, R.id.nav_lideres, R.id.nav_bitacora
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //SETEAR EL MENU OVERFLOW
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow, menu)
        return true
    }

    //ITEMS DEL MENU OVERFLOW - CERRAR SESION
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // CERRAR SESION
        if (item.itemId == R.id.menu_actividad2) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        if (item.itemId == R.id.menu_actividad){
            //findNavController().navigate(R.id.action_perfilFragment)
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        if (item.itemId == R.id.menu_actividad3){
            //findNavController().navigate(R.id.action_perfilFragment)
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}