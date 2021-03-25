package com.example.runnerwar.ui.seleccionFaccion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.NavActivity
import com.example.runnerwar.R
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.ui.registro.RegistroViewModelFactory


import kotlinx.android.synthetic.main.activity_seleccion_faccion.*


class SeleccionFaccionActivity : AppCompatActivity() {

    private lateinit var  selFaccViewModel: SeleccionFaccionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_faccion)

        val repository = RegistroRepository()
        val viewModelFactory = RegistroViewModelFactory(repository,  2)

        selFaccViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SeleccionFaccionViewModel::class.java)

        var username = intent.extras?.getString("username")
        var email = intent.extras?.getString("email")
        var password = intent.extras?.getString("password")


        selFaccViewModel.responseCreate.observe(this@SeleccionFaccionActivity, Observer { response ->

            if (response.isSuccessful){
                val data: UserResponse? = response.body()



                if (data != null) {
                    Log.d("Response", data._id)
                    Log.d("Response", data.accountname)
                    Log.d("Response", data.password.toString())
                    Log.d("Response", data.coins.toString())
                    Log.d("Response", data.faction.toString())
                    Log.d("Response", data.points.toString())

                    val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
                    intent.putExtra("email", data._id)
                    intent.putExtra("coins", data.coins)
                    intent.putExtra("faction", data.faction)
                    intent.putExtra("password", data.password)
                    intent.putExtra("points", data.points)
                    intent.putExtra("username", data.accountname)
                    startActivity(intent)

                }




            }
            //Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()

        })

        imageButton2.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "yellow")
            selFaccViewModel.signUp(user)
            /*val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)*/
        }

        imageButton4.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "blue")
            selFaccViewModel.signUp(user)
            /*val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)*/
        }

        imageButton5.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "red")
            selFaccViewModel.signUp(user)
            /*val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)*/
        }

        imageButton6.setOnClickListener{
            var user : UserForm = UserForm(email.toString(),username.toString(),password.toString(), "green")
            selFaccViewModel.signUp(user)

           /* val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
            startActivity(intent)*/
        }


    }
}

