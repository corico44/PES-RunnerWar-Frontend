package com.example.runnerwar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.runnerwar.ui.main.NavFragment

class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NavFragment.newInstance())
                .commitNow()
        }
    }
}