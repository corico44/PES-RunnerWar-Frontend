package com.example.runnerwar

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runnerwar.Services.ContarPasosService
import com.example.runnerwar.ui.login.LoginActivity
import com.example.runnerwar.ui.mapa.MapaFragment
import com.example.runnerwar.ui.registro.RegistroActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //val intent = Intent(this@MainActivity, RegistroActivity::class.java)

        intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.putExtra("some_error", " ")
        startActivity(intent)


    }
}