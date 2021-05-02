package com.example.runnerwar.ui.cambiarFaccion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.DeleteUser
import com.example.runnerwar.Model.FactionForm
import com.example.runnerwar.Model.RegisterResponse
import com.example.runnerwar.Model.UserUpdate
import com.example.runnerwar.NavActivity
import com.example.runnerwar.R
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.cambiarFaccion.CambiarFaccionActivity
import com.example.runnerwar.ui.cuenta.CuentaFragment
import com.example.runnerwar.ui.registro.RegistroActivity
import com.example.runnerwar.util.Session
import kotlinx.android.synthetic.main.fragment_cuenta.*
import kotlinx.android.synthetic.main.fragment_cuenta.reg_email
import kotlinx.android.synthetic.main.fragment_cuenta.reg_userName
import kotlinx.android.synthetic.main.registro.*
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton2
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton4
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton5
import kotlinx.android.synthetic.main.activity_cambiar_faccion.imageButton6

class CambiarFaccionActivity : AppCompatActivity(){
    private lateinit var  selCambiarFacViewModel: CambiarFaccionViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_faccion)


        val userDao = UserDataBase.getDataBase(application).userDao()
        val repository = UserRepository(userDao, Session.getIdUsuario())
        val viewModelFactory = UserViewModelFactory(repository,4)
        selCambiarFacViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CambiarFaccionViewModel::class.java)

        selCambiarFacViewModel.responseChangeFaction.observe(this@CambiarFaccionActivity, Observer{ response ->
            if (response.isSuccessful){
                Toast.makeText(applicationContext, "Update faction successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, CuentaFragment::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext, "Update faction failed", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, CuentaFragment::class.java)
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