package com.example.runnerwar.ui.registro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
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
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.Repositories.UserRepository
import kotlinx.android.synthetic.main.login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var LoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val userDao = UserDataBase.getDataBase(application).userDao()
        val repository = UserRepository(userDao,"null")
        val viewModelFactory = UserViewModelFactory(repository, 3)

        LoginViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val email = findViewById<EditText>(R.id.cuenta_email)
        val password = findViewById<EditText>(R.id.password_signup)
        val login = findViewById<Button>(R.id.signup_button)

        login.isEnabled = false
        login.isClickable = false



        LoginViewModel.registroFormState.observe(this@LoginActivity, Observer {
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

        LoginViewModel.responseCreate.observe(this@LoginActivity, Observer { response ->

            if (response.isSuccessful){
                val data: Codi? = response.body()

                if (data != null) {
                    if (data.result.equals(200)) {
                        Toast.makeText(applicationContext, "Welcome to RunnerWar", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, NavActivity::class.java)
                        intent.putExtra("email", email.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Login error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })

        email.afterTextChanged {
            LoginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            LoginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        CreateAccButto.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistroActivity::class.java)
        }
        signup_button.setOnClickListener {
            var lu : LoginUser = LoginUser(email.text.toString(), password.text.toString())
            LoginViewModel.logIn(lu)
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

