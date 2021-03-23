package com.example.runnerwar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.ui.registro.RegistroViewModel
import com.example.runnerwar.ui.registro.RegistroViewModelFactory
import com.example.runnerwar.ui.registro.SeleccionFaccionViewModel


import kotlinx.android.synthetic.main.activity_seleccion_faccion.*


class SeleccionFaccionActivity : AppCompatActivity() {

    private lateinit var  selFaccViewModel: SeleccionFaccionViewModel

    val repository = RegistroRepository()
    val viewModelFactory = RegistroViewModelFactory(repository)

    selFaccViewModel = ViewModelProviders.of(this, viewModelFactory)
    .get(SeleccionFaccionViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_faccion)

        var username = intent.extras?.getString("username")
        var email = intent.extras?.getString("email")
        var password = intent.extras?.getString("password")



        imageButton2.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "yellow")
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }

        imageButton4.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "blue")
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }

        imageButton5.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "red")
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }

        imageButton6.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "green")
            val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)
        }


    }
}

