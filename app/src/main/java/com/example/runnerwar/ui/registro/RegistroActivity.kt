package com.example.runnerwar.ui.registro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Model.UserResponse


import com.example.runnerwar.R
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionActivity


class RegistroActivity : AppCompatActivity() {

    private lateinit var  registroViewModel: RegistroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        val repository = RegistroRepository()
        val viewModelFactory = RegistroViewModelFactory(repository, 1)

        registroViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(RegistroViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val username = findViewById<EditText>(R.id.reg_userName)
        val email = findViewById<EditText>(R.id.reg_email)
        val password = findViewById<EditText>(R.id.reg_password)
        val conditions = findViewById<CheckBox>(R.id.reg_checkbox)
        val signup = findViewById<Button>(R.id.signup_button)

        signup.isEnabled = false
        signup.isClickable=false



        registroViewModel.registroFormState.observe(this@RegistroActivity, Observer{
            val registroForm = it ?: return@Observer

            if(!registroForm.isDataValid){
                if (registroForm.usernameError != null){
                    username.error = registroForm.usernameError
                }
                if (registroForm.emailError != null){
                    email.error = registroForm.emailError
                }
                if (registroForm.passwordError != null){
                    password.error = registroForm.passwordError
                }
            }
            else{
                signup.isEnabled = true
                signup.isClickable=true
            }

        })

        username.afterTextChanged {
            registroViewModel.singUpDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        email.afterTextChanged {
            registroViewModel.singUpDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            registroViewModel.singUpDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        signup.setOnClickListener{
            //registroViewModel.signUp(UserForm( username.text.toString(), email.text.toString(),password.text.toString(), "rojo") )
            val intent = Intent(this@RegistroActivity, SeleccionFaccionActivity::class.java)
            intent.putExtra("username", username.text.toString())
            intent.putExtra("email", email.text.toString())
            intent.putExtra("password", password.text.toString())
            startActivity(intent)
        }

    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

