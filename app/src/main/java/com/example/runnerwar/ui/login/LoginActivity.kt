package com.example.runnerwar.ui.login


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.UserViewModelFactory
import com.example.runnerwar.Model.LoginUser
import com.example.runnerwar.NavActivity

import com.google.firebase.auth.ktx.auth


import com.example.runnerwar.R
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroActivity
import com.example.runnerwar.util.Language
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
        val titulo_login = findViewById<TextView>(R.id.editTextTextPersonName)
        val titulo_password = findViewById<TextView>(R.id.textView10)
        val password = findViewById<EditText>(R.id.password_signup)
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        val texto_boton_login = signInButton.getChildAt(0) as TextView
        val login = findViewById<Button>(R.id.signup_button)
        val error = findViewById<TextView>(R.id.error)
        val ingles = findViewById<ImageButton>(R.id.ingles)
        val castellano = findViewById<ImageButton>(R.id.castellano)

        texto_boton_login.setText("Log in")
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
                if(Language.idioma.equals("castellano")){
                    error.text = "Email o contraseña incorrecta"
                }
                else if(Language.idioma.equals("ingles")){
                    error.text = "Email or password incorrect"
                }
                else{
                    error.text = "Email o contraseña incorrecta"
                }
            }
        })


        loginViewModel.responseActivity.observe(this@LoginActivity, Observer {
            val statReg = it ?: return@Observer
            if (statReg.codi == 200){
                if(Language.idioma.equals("castellano")){
                    Toast.makeText(applicationContext, "Bienvenido a RunnerWar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, NavActivity::class.java)
                    startActivity(intent)
                }
                else if(Language.idioma.equals("ingles")){
                    Toast.makeText(applicationContext, "Welcome to RunnerWar", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, NavActivity::class.java)
                    startActivity(intent)
                }
                else {
                    error.text = "Idioma no seleccionado"
                }
            }
            else {
                if(Language.idioma.equals("castellano")){
                    error.text = "Email o contraseña incorrecta"
                }
                else if(Language.idioma.equals("ingles")){
                    error.text = "Email or password incorrect"
                }
                else{
                    error.text = "Email o contraseña incorrecta"
                }
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
            if(!Language.idioma.equals("none")){
                if(user != null){
                    var lu : LoginUser = LoginUser(user.email.toString(), password)
                    loginViewModel.logIn(lu)
                }

            }
            else {
                error.text = "Idioma no seleccionado"
            }

        }
        ingles.setOnClickListener {
            Language.idioma = "ingles"
            titulo_login.setText("LOG IN")
            titulo_password.setText("Password")
            CreateAccButto.setText("Newbie? Create account")
            signup_button.setText("PROCEED")
            texto_boton_login.setText("Log in")
        }
        castellano.setOnClickListener {
            Language.idioma = "castellano"
            titulo_login.setText("ACCESO")
            titulo_password.setText("Contraseña")
            CreateAccButto.setText("¿Eres nuevo? Crear cuenta")
            signup_button.setText("PROCEDER")
            texto_boton_login.setText("Iniciar sesión")
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

