package com.example.runnerwar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import kotlinx.android.synthetic.main.activity_seleccion_faccion.*


class SeleccionFaccionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_faccion)

        imageButton2.setOnClickListener{
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }

        imageButton4.setOnClickListener{
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }

        imageButton5.setOnClickListener{
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }

        imageButton6.setOnClickListener{
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }

    }
}

