package com.example.runnerwar.ui.seleccionFaccion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.NavActivity
import com.example.runnerwar.R
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroActivity


import kotlinx.android.synthetic.main.activity_seleccion_faccion.*

class SeleccionFaccionActivity : AppCompatActivity() {

    private lateinit var  selFaccViewModel: SeleccionFaccionViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_faccion)


        var username = intent.extras?.getString("username")
        var email = intent.extras?.getString("email")
        var password = intent.extras?.getString("password")


        val userDao = UserDataBase.getDataBase(application).userDao()
        val repository = UserRepository(userDao,email.toString())
        val viewModelFactory = UserViewModelFactory(repository,1)
        selFaccViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SeleccionFaccionViewModel::class.java)



        /*selFaccViewModel.responseCreate.observe(this@SeleccionFaccionActivity, Observer {
            val statReg = it ?: return@Observer
            if (statReg.codi == 200){
                Toast.makeText(applicationContext, "Welcome to RunnerWar $username", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@SeleccionFaccionActivity, RegistroActivity::class.java)
                intent.putExtra("some_error", "Email:  $email  or Username: $username already exists")
                startActivity(intent)
            }
        })*/

        selFaccViewModel.responseCreate.observe(this@SeleccionFaccionActivity, Observer {
            val statReg = it ?: return@Observer
            if (statReg.codi == 200){
                selFaccViewModel.initServiceContarPasos(application)
            }
            else {
                val intent = Intent(this@SeleccionFaccionActivity, RegistroActivity::class.java)
                intent.putExtra("some_error", "Email:  $email  or Username: $username already exists")
                startActivity(intent)
            }
        })


        selFaccViewModel.responseActivity.observe(this@SeleccionFaccionActivity, Observer {
            val stat= it ?: return@Observer
            if(stat.codi ==200){
                Toast.makeText(applicationContext, "Welcome to RunnerWar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
                startActivity(intent)
            }
        })



        imageButton2.setOnClickListener{
            var password = selFaccViewModel.hashString(password.toString(),"SHA-256")
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "yellow")
            Log.d("Enc_pas ", password)
            selFaccViewModel.signUp(user)

        }

        imageButton4.setOnClickListener{
            var password = selFaccViewModel.hashString(password.toString(),"SHA-256")
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "blue")
            Log.d("Enc_pas ", password)
            selFaccViewModel.signUp(user)

        }

        imageButton5.setOnClickListener{
            var password = selFaccViewModel.hashString(password.toString(),"SHA-256")
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "red")
            Log.d("Enc_pas ", password)
            selFaccViewModel.signUp(user)

        }

        imageButton6.setOnClickListener{
            var password = selFaccViewModel.hashString(password.toString(),"SHA-256")
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "green")
            Log.d("Enc_pas ", password)
            selFaccViewModel.signUp(user)
        }


    }

}


