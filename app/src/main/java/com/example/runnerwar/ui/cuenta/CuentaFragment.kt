package com.example.runnerwar.ui.cuenta

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Model.DeleteUser
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.Model.UserUpdate
import com.example.runnerwar.R
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.ui.registro.RegistroActivity
import com.example.runnerwar.ui.registro.RegistroViewModelFactory
import kotlinx.android.synthetic.main.fragment_cuenta.*
import kotlinx.android.synthetic.main.fragment_cuenta.reg_email
import kotlinx.android.synthetic.main.fragment_cuenta.reg_userName
import kotlinx.android.synthetic.main.registro.*

class CuentaFragment : Fragment() {

    private lateinit var cuentaViewModel: CuentaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val repository = RegistroRepository()
        val viewModelFactory = RegistroViewModelFactory(repository,  3)


        cuentaViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CuentaViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_cuenta, container, false)
        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username : String? = activity?.intent?.extras?.getString("username")
        var email : String? = activity?.intent?.extras?.getString("email")
        var faction : String? = activity?.intent?.extras?.getString("faction")
        var points : String? = activity?.intent?.extras?.getString("points")

        reg_userName.setText(username.toString())
        reg_email.setText(email.toString())
        reg_faction.setText(faction.toString())
        reg_points.setText(points.toString())

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

        cuentaViewModel.responseUpdate.observe(this@CuentaFragment, Observer { response ->

            if (response.isSuccessful){
                val data: UserResponse? = response.body()
                if (data != null) {
                    reg_userName.setText(data.accountname.toString())
                    Toast.makeText(activity?.applicationContext, "Success", Toast.LENGTH_SHORT).show()
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
            cuentaViewModel.updateUser(userUp)
        }

        boton_eliminar.setOnClickListener{
            val user = DeleteUser(email.toString())
            cuentaViewModel.deleteUser(user)

        }
    }
}