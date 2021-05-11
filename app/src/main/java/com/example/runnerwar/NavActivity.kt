package com.example.runnerwar

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.handy.opinion.utils.LocationHelper
import kotlinx.android.synthetic.main.fragment_cuenta.*

class NavActivity : AppCompatActivity() {

    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        email = intent.extras?.getString("email").toString()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /* val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_muro, R.id.navigation_mapa, R.id.navigation_chat, R.id.navigation_calendario, R.id.navigation_cuenta
            )
        )*/
        navView.setupWithNavController(navController)

    }

    fun getMyEmail(): String? {
        return email
    }

}