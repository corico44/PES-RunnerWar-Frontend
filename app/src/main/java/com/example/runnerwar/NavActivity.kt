package com.example.runnerwar

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.runnerwar.Services.ContarPasosService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.fragment_cuenta.*

class NavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        var logged_user = intent.extras?.getString("email")

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        /*ActivityCompat.requestPermissions(this@NavActivity,
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            ContarPasosService.RECORD_REQUEST_CODE
        )*/

        ContarPasosService.responsePasos.observe(this, Observer { response ->

            pasosTextView.text = response.toString()

        })



        Intent(this, ContarPasosService::class.java).also {
            startService(it)
        }

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