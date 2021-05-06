package com.example.runnerwar

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.runnerwar.Data.DailyActivity.ActivityDataBase
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.ActivityViewModelFactory
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.Services.ContarPasosService
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.fragment_cuenta.*
import kotlinx.coroutines.runBlocking

class NavActivity : AppCompatActivity() {

    private lateinit var navViewModel: NavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val activityDao = ActivityDataBase.getDataBase(application).activityDao()
        val repository: ActivityRepository = ActivityRepository(activityDao)
        val viewModelFactory = ActivityViewModelFactory(repository)
        navViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NavViewModel::class.java)


        //navViewModel.initServiceContarPasos()

        Intent(this, ContarPasosService::class.java).also {
            ContarPasosService.context = application
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

     override fun onStop() {
        super.onPause()
        navViewModel.updateActivityData()
    }

}