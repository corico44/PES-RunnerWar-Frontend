package com.example.runnerwar.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.LoginUser
import com.example.runnerwar.NavActivity

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.Task
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.SignInButton


import com.example.runnerwar.R
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroActivity
import com.example.runnerwar.util.Session
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.login.*



class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        val userDao = UserDataBase.getDataBase(application).userDao()
        val repository = UserRepository(userDao,"null")
        val viewModelFactory = UserViewModelFactory(repository, 6)

        loginViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val email = findViewById<EditText>(R.id.cuenta_email)
        val password = findViewById<EditText>(R.id.password_signup)
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
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
            if (statReg.codi == 200){
                loginViewModel.initServiceContarPasos(application)
            }
            else {
                error.text = "Email or password incorrect"
            }
        })


        loginViewModel.responseActivity.observe(this@LoginActivity, Observer {
            val statReg = it ?: return@Observer
            if (statReg.codi == 200){
                Toast.makeText(applicationContext, "Welcome to RunnerWar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, NavActivity::class.java)
                startActivity(intent)
            }
            else {

                error.text = "Email or password incorrect"
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
        signInButton.setOnClickListener{
            var user = auth.currentUser
            var password = loginViewModel.hashString("google","SHA-256")
            if(user != null){
                var lu : LoginUser = LoginUser(user.email.toString(), password)
                loginViewModel.logIn(lu)
            }

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

