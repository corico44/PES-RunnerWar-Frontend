package com.runnerwar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.runnerwar.Data.DailyActivity.ActivityDataBase
import com.runnerwar.Factories.ActivityViewModelFactory
import com.runnerwar.R
import com.runnerwar.Repositories.ActivityRepository
import com.runnerwar.Services.ContarPasosService
import com.google.android.material.bottomnavigation.BottomNavigationView

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
         super.onStop()
         super.onPause()
        navViewModel.updateActivityData()
    }

}