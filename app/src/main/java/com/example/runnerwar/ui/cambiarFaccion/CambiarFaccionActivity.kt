package com.example.runnerwar.ui.cambiarFaccion

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.FactionForm
import com.example.runnerwar.NavActivity
import com.example.runnerwar.R
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.util.Language
import com.example.runnerwar.util.Session
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton2
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton4
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton5
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton6

class CambiarFaccionActivity : AppCompatActivity(){
    private lateinit var  selCambiarFacViewModel: CambiarFaccionViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_faccion)

        val titulo = findViewById<TextView>(R.id.titulo)
        val texto_amarillo = findViewById<TextView>(R.id.texto_amarillo)
        val texto_rojo = findViewById<TextView>(R.id.texto_rojo)
        val texto_azul = findViewById<TextView>(R.id.texto_azul)
        val texto_verde = findViewById<TextView>(R.id.texto_verde)

        val userDao = UserDataBase.getDataBase(application).userDao()
        val repository = UserRepository(userDao, Session.getIdUsuario())
        val viewModelFactory = UserViewModelFactory(repository,4)
        selCambiarFacViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CambiarFaccionViewModel::class.java)

        if(Language.idioma.equals("castellano")){
            titulo.setText("¡CAMBIA DE EQUIPO!")
            texto_amarillo.setText("¡La     guerra     y     la     paz     pueden     ir     de     la     mano     si     quieres     luchar     por     la     paz     de     toda     la     facción     amarilla     que     te     espera!")
            texto_azul.setText("Todos     somos     iguales     pero     la     faccion     azul     lucha     hasta     que     ya     no     pueden.     ¡Unete     a     nosotros     y     lucha     por     una     Barcelona     mejor!")
            texto_rojo.setText("Esto     no     es     un     juego,     es     una     confrontacion     real.     ¡Si     quieres     ganar,     unete     a     nosotros!")
            texto_verde.setText("Juntos     dominaremos     toda     Barcelona     y     todas     las     facciones     sabran     que     el     verde     es     el     mejor.     ¡Ven     con     nosotros!")
        }
        else if(Language.idioma.equals("ingles")){
            titulo.setText("CHANGE YOUR TEAM!")
            texto_amarillo.setText("War     and     peace     can     go     hand     in     hand     if     u     want     to     fight     for     the     peace     of     all     the     yellow     faction     awaits     you!")
            texto_azul.setText("We     are     all     the     same     but     the     blue     faction     fights     until     they     can     no     longer.     Join     us     and     fight     for     a     better     Barcelona!")
            texto_rojo.setText("This     is     not     a     game,     this     is     a     real     confrontation.     If     you     wanna     win     then     join     us!")
            texto_verde.setText("Together     we     will     dominate     all     of     Barcelona     and     all     factions     will     know     that     green     is     the     best     one.     Come     with     us!")
        }

        selCambiarFacViewModel.responseChangeFaction.observe(this@CambiarFaccionActivity, Observer{ response ->
            if (response.isSuccessful){
                Toast.makeText(applicationContext, "Update faction successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, NavActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext, "Update faction failed", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, NavActivity::class.java)
                startActivity(intent)
            }

        })

        imageButton2.setOnClickListener{
            var faction : FactionForm = FactionForm(Session.getIdUsuario(),"yellow")
            selCambiarFacViewModel.changeFaction(faction)

        }

        imageButton4.setOnClickListener{
            var faction :FactionForm = FactionForm(Session.getIdUsuario(),"blue")
            selCambiarFacViewModel.changeFaction(faction)

        }

        imageButton5.setOnClickListener{
            var faction :FactionForm = FactionForm(Session.getIdUsuario(),"red")
            selCambiarFacViewModel.changeFaction(faction)

        }

        imageButton6.setOnClickListener{
            var faction :FactionForm = FactionForm(Session.getIdUsuario(),"green")
            selCambiarFacViewModel.changeFaction(faction)
        }


    }
}