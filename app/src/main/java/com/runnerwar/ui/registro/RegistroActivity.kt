package com.runnerwar.ui.registro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders


import com.runnerwar.R
import com.runnerwar.Repositories.RegistroRepository
import com.runnerwar.ui.login.LoginActivity
import com.runnerwar.ui.seleccionFaccion.SeleccionFaccionActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.registro.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import android.util.Log
import android.widget.ImageButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.SignInButton


class RegistroActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var message_eror: TextView

    private lateinit var  registroViewModel: RegistroViewModel

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        //updateUI(account)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1054517802738-1ra88vp8rnhfg8qk23i4sklios7i1m6d.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.



        // Build a GoogleSignInClient with the options specified by gso.

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
        val signup = findViewById<Button>(R.id.signup_button)
        val error = findViewById<TextView>(R.id.error)
        val titulo_signup = findViewById<TextView>(R.id.editTextTextPersonName)
        val titulo_username = findViewById<TextView>(R.id.textView8)
        val titulo_password = findViewById<TextView>(R.id.textView10)
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        val texto_boton_login = signInButton.getChildAt(0) as TextView
        val ingles = findViewById<ImageButton>(R.id.ingles)
        val castellano = findViewById<ImageButton>(R.id.castellano)
        //signInButton.setSize(signInButton.SIZE_STANDARD);
        val some_error = intent.extras?.getString("some_error")

        texto_boton_login.setText("Sign up")
        error.text = some_error
        message_eror = error

        signup.isEnabled = false
        signup.isClickable=false


        signInButton.setOnClickListener {
            Log.w("1", "entra click listener")
            signIn()
        }

        ingles.setOnClickListener {
            com.runnerwar.util.Language.idioma = "ingles"
            titulo_signup.setText("SIGN UP")
            titulo_username.setText("Username")
            titulo_password.setText("Password")
            signup.setText("PROCEED")
            ex_us_login.setText("EXISTING USER? LOG IN")
            texto_boton_login.setText("Sign up")
        }
        castellano.setOnClickListener {
            com.runnerwar.util.Language.idioma = "castellano"
            titulo_signup.setText("REGISTRO")
            titulo_username.setText("Nombre de usuario")
            titulo_password.setText("Contraseña")
            signup.setText("PROCEDER")
            ex_us_login.setText("USUARIO EXISTENTE? INICIAR SESIÓN")
            texto_boton_login.setText("Registrarse")
        }


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

        ex_us_login.setOnClickListener {
            val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        signup.setOnClickListener{
            //registroViewModel.signUp(UserForm( username.text.toString(), email.text.toString(),password.text.toString(), "rojo") )
            if(!com.runnerwar.util.Language.idioma.equals("none")){
                val intent = Intent(this@RegistroActivity, SeleccionFaccionActivity::class.java)
                intent.putExtra("username", username.text.toString())
                intent.putExtra("email", email.text.toString())
                intent.putExtra("password", password.text.toString())
                startActivity(intent)
            }
            else {
                error.text = "Idioma no seleccionado"
            }
        }

    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            Log.w("3", "entra try")
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            Log.w("4", "hola")
            // Signed in successfully, show authenticated UI.
            val intent = Intent(this@RegistroActivity, SeleccionFaccionActivity::class.java)
            if(!com.runnerwar.util.Language.idioma.equals("none")){
                if (account != null) {
                    intent.putExtra("username", account.displayName.toString())
                    intent.putExtra("email", account.email.toString())
                    intent.putExtra("password", account.hashCode().toString())
                    startActivity(intent)
                }
            }
            else {
                message_eror.text = "Idioma no seleccionado"
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            val intent = Intent(this@RegistroActivity,this@RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        Log.w("2", "entra result")
        if (requestCode == RC_SIGN_IN) {  if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("success", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("fail", "Google sign in failed", e)
            }
        }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.w("2", "entra auth with google")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("success", "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent = Intent(this@RegistroActivity, SeleccionFaccionActivity::class.java)
                    if(!com.runnerwar.util.Language.idioma.equals("none")){
                        if (user != null) {
                            intent.putExtra("username", user.displayName.toString())
                            intent.putExtra("email", user.email.toString())
                            intent.putExtra("password", "google")
                            startActivity(intent)
                        }
                    }
                    else {
                        message_eror.text = "Idioma no seleccionado"
                    }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("fail", "signInWithCredential:failure", task.exception)
                    val intent = Intent(this@RegistroActivity,this@RegistroActivity::class.java)
                    startActivity(intent)
                }
            }
    }

    fun signIn() {
        Log.w("2", "entra sign in")
        val signInIntent = mGoogleSignInClient.signInIntent
        Log.w("2.1", "pasa sign in")
        Log.w("signinintent", signInIntent.toString())

        val result = startActivityForResult(signInIntent, RC_SIGN_IN)
        Log.w("res", result.toString())
        Log.w("2.3", "pasa activity for result")
    }

    companion object {
        private const val RC_SIGN_IN = 9001
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
            println("Update")}

        /**
         * esta funcion indica que el texto ha cambiado
         */
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { println("Update")}
    })
}

