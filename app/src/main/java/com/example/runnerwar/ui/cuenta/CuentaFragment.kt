package com.example.runnerwar.ui.cuenta

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.DeleteUser
import com.example.runnerwar.Model.RegisterResponse
import com.example.runnerwar.Model.UserUpdate
import com.example.runnerwar.R
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.buscarCuenta.SearchFragment
import com.example.runnerwar.ui.cambiarFaccion.CambiarFaccionActivity
import com.example.runnerwar.ui.registro.RegistroActivity
import io.getstream.chat.android.client.ChatClient
import com.example.runnerwar.util.Session
import kotlinx.android.synthetic.main.fragment_cuenta.*
import kotlinx.android.synthetic.main.fragment_cuenta.reg_email
import kotlinx.android.synthetic.main.fragment_cuenta.reg_userName
import kotlinx.android.synthetic.main.registro.*

class CuentaFragment : Fragment() {

    private lateinit var cuentaViewModel: CuentaViewModel
    private lateinit var loggedUser: String
    //private val client = ChatClient.instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        //Get logged User id

        loggedUser = Session.getIdUsuario()
        //Ini Cuenta viewModel
        val userDao = UserDataBase.getDataBase(activity?.application!!).userDao()
        val repository = UserRepository(userDao, loggedUser.toString())
        val viewModelFactory = UserViewModelFactory(repository,2)
        cuentaViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CuentaViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_cuenta, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var actualname: String = "null"

        //var loggedUser : String? = Session.getIdUsuario()

        boton_edit.setOnClickListener {
            if (reg_userName.isEnabled) {
                reg_userName.setEnabled(false)
                disk_save.setVisibility(View.INVISIBLE)
            }
            else if(!reg_userName.isEnabled){
                reg_userName.setEnabled(true)
                disk_save.setVisibility(View.VISIBLE)
            }
        }

        cambiarFaccion.setOnClickListener{
            val intent = Intent(activity?.applicationContext, CambiarFaccionActivity::class.java)
            startActivity(intent)
        }

        cuentaViewModel.readAllData.observe(this@CuentaFragment, Observer { user ->
            if (user != null){
                reg_userName.setText(user.accountname)
                actualname = user.accountname
                reg_email.setText(user._id)
                reg_faction.setText(user.faction)
                reg_points.setText(user.points.toString())
            }

        })


        cuentaViewModel.responseUpdate.observe(this@CuentaFragment, Observer { response ->

            if (response.isSuccessful){
                val data: RegisterResponse? = response.body()
                if (data != null) {
                    if (data.codi == 200) {
                        Toast.makeText(activity?.applicationContext, "Update successfully", Toast.LENGTH_SHORT).show()
                        actualname = data.accountname.toString()
                    }
                    else {
                        Toast.makeText(activity?.applicationContext, "Accountname already used", Toast.LENGTH_SHORT).show()
                        reg_userName.setText(actualname)
                    }
                }
            }
        })

        cuentaViewModel.responseDelete.observe(this@CuentaFragment, Observer { response ->

            if (response.isSuccessful){
                val intent = Intent(activity?.applicationContext, RegistroActivity::class.java)
                startActivity(intent)
            }
        })


       disk_save.setOnClickListener {
            reg_userName.setEnabled(false)
            disk_save.setVisibility(View.INVISIBLE)
            val userUp = UserUpdate(reg_userName.text.toString(), reg_email.text.toString())
            if(userUp != null) cuentaViewModel.updateUser(userUp)
        }

        boton_eliminar.setOnClickListener{
            val user = DeleteUser(loggedUser.toString())
            if(user != null) cuentaViewModel.deleteUser(user)
        }

        boton_logout.setOnClickListener {
            //client.disconnect()
            val intent = Intent(activity?.applicationContext, RegistroActivity::class.java)
            startActivity(intent)
        }

        boton_search.setOnClickListener{
            //androidx.appcompat.widget.AppCompatImageButton cannot be cast to android.view.ViewGroup
            val transaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
            transaction.replace(R.id.nav_host_fragment,SearchFragment()).commit()
        }
    }
}