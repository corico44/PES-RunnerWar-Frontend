package com.runnerwar.ui.seleccionFaccion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.runnerwar.Data.User.UserDataBase
import com.runnerwar.Factories.UserViewModelFactory
import com.runnerwar.Model.UserForm
import com.runnerwar.NavActivity
import com.runnerwar.R
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.ui.registro.RegistroActivity
import com.runnerwar.util.Language


import kotlinx.android.synthetic.main.activity_seleccion_faccion.*

class SeleccionFaccionActivity : AppCompatActivity() {

    private lateinit var  selFaccViewModel: SeleccionFaccionViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_faccion)


        var username = intent.extras?.getString("username")
        var email = intent.extras?.getString("email")
        var password = intent.extras?.getString("password")

        val titulo = findViewById<TextView>(R.id.titulo)
        val textoAmarillo = findViewById<TextView>(R.id.texto_amarillo)
        val textoRojo = findViewById<TextView>(R.id.texto_rojo)
        val textoAzul = findViewById<TextView>(R.id.texto_azul)
        val textoVerde = findViewById<TextView>(R.id.texto_verde)

        val userDao = UserDataBase.getDataBase(application).userDao()
        val repository = UserRepository(userDao,email.toString())
        val viewModelFactory = UserViewModelFactory(repository,1)
        selFaccViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SeleccionFaccionViewModel::class.java)


        if(Language.idioma.equals("castellano")){
            titulo.text = "ELIGE UN EQUIPO!"
            textoAmarillo.text = "¡La     guerra     y     la     paz     pueden     ir     de     la     mano     si     quieres     luchar     por     la     paz     de     toda     la     facción     amarilla     que     te     espera!"
            textoAzul.text = "Todos     somos     iguales     pero     la     faccion     azul     lucha     hasta     que     ya     no     pueden.     ¡Unete     a     nosotros     y     lucha     por     una     Barcelona     mejor!"
            textoRojo.text = "Esto     no     es     un     juego,     es     una     confrontacion     real.     ¡Si     quieres     ganar,     unete     a     nosotros!"
            textoVerde.text = "Juntos     dominaremos     toda     Barcelona     y     todas     las     facciones     sabran     que     el     verde     es     el     mejor.     ¡Ven     con     nosotros!"
        }
        else if(Language.idioma.equals("ingles")){
            titulo.text = "CHOOSE A TEAM!"
            textoAmarillo.text = "War     and     peace     can     go     hand     in     hand     if     u     want     to     fight     for     the     peace     of     all     the     yellow     faction     awaits     you!"
            textoAzul.text = "We     are     all     the     same     but     the     blue     faction     fights     until     they     can     no     longer.     Join     us     and     fight     for     a     better     Barcelona!"
            textoRojo.text = "This     is     not     a     game,     this     is     a     real     confrontation.     If     you     wanna     win     then     join     us!"
            textoVerde.text = "Together     we     will     dominate     all     of     Barcelona     and     all     factions     will     know     that     green     is     the     best     one.     Come     with     us!"
        }

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
                if(Language.idioma.equals("castellano")){
                    Toast.makeText(applicationContext, "Bienvenido a RunnerWar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
                    startActivity(intent)
                }
                if(Language.idioma.equals("ingles")){
                    Toast.makeText(applicationContext, "Welcome to RunnerWar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SeleccionFaccionActivity, NavActivity::class.java)
                    startActivity(intent)
                }
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


