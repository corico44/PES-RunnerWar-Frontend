package com.example.runnerwar.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.LoginUser
import com.example.runnerwar.Model.User
import com.example.runnerwar.NavActivity


import com.example.runnerwar.R
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroActivity
import com.example.runnerwar.util.Session
import kotlinx.android.synthetic.main.login.*



class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val userDao = UserDataBase.getDataBase(application).userDao()
        val repository = UserRepository(userDao,"null")
        val viewModelFactory = UserViewModelFactory(repository, 3)

        loginViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val email = findViewById<EditText>(R.id.cuenta_email)
        val password = findViewById<EditText>(R.id.password_signup)
        val login = findViewById<Button>(R.id.signup_button)
        val error = findViewById<TextView>(R.id.error)

        login.isEnabled = true
        login.isClickable = true



        loginViewModel.registroFormState.observe(this@LoginActivity, Observer {
            val registroForm = it ?: return@Observer

            if (!registroForm.isDataValid) {
                if (registroForm.emailError != null) {
                    email.error = registroForm.emailError
                }
                if (registroForm.passwordError != null) {
                    password.error = registroForm.passwordError
                }
            } else {
                login.isEnabled = true
                login.isClickable = true
            }

        })

        loginViewModel.responseCreate.observe(this@LoginActivity, Observer {
            val statReg = it ?: return@Observer
            if (statReg.result == 200){
                loginViewModel.initServiceContarPasos(application)
            }
            else {
                error.text = "Email or password incorrect"
            }
        })


        loginViewModel.responseActivity.observe(this@LoginActivity, Observer {
            val stat= it ?: return@Observer
            if(stat.result ==200){
                Toast.makeText(applicationContext, "Welcome to RunnerWar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, NavActivity::class.java)
                startActivity(intent)
            }
        })





        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        CreateAccButto.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistroActivity::class.java)
            startActivity(intent)
        }
        signup_button.setOnClickListener {
            var password = loginViewModel.hashString(password.text.toString(),"SHA-256")
            var lu : LoginUser = LoginUser(email.text.toString(), password.toString())
            Log.d("Enc_pas ", password)
            loginViewModel.logIn(lu)
            //val intent = Intent(this@LoginActivity, NavActivity::class.java)
            //startActivity(intent)
        }

    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            /**
             * esta funcion se llama antes de que el texto cambie
             **/
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                println("Update")
            }

            /**
             * esta funcion indica que el texto ha cambiado
             */
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println("Update")
            }
        })
    }
}

