package com.example.runnerwar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_cuenta.*

class NavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        var logged_user = intent.extras?.getString("email")

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

}