package com.example.runnerwar.ui.registro

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


import com.example.runnerwar.R
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.ui.login.LoginActivity
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.registro.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.Task
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.SignInButton


class RegistroActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 10

    private lateinit var auth: FirebaseAuth

    private lateinit var  registroViewModel: RegistroViewModel

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val currentUser = auth.currentUser

        //updateUI(account)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

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
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        //signInButton.setSize(signInButton.SIZE_STANDARD);
        val some_error = intent.extras?.getString("some_error")

        error.text = some_error

        signup.isEnabled = false
        signup.isClickable=false


        signInButton.setOnClickListener {
            Log.w("1", "entra click listener")
            signIn()
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
            val intent = Intent(this@RegistroActivity, SeleccionFaccionActivity::class.java)
            intent.putExtra("username", username.text.toString())
            intent.putExtra("email", email.text.toString())
            intent.putExtra("password", password.text.toString())
            startActivity(intent)
        }

    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            Log.w("3", "entra try")
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            Log.w("4", "hola")
            // Signed in successfully, show authenticated UI.
            val intent = Intent(this@RegistroActivity, SeleccionFaccionActivity::class.java)
            if (account != null) {
                intent.putExtra("username", account.displayName.toString())
                intent.putExtra("email", account.email.toString())
                intent.putExtra("password", account.hashCode().toString())
            }
            startActivity(intent)
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
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("success", "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent = Intent(this@RegistroActivity, SeleccionFaccionActivity::class.java)
                    if (user != null) {
                        intent.putExtra("username", user.displayName.toString())
                        intent.putExtra("email", user.email.toString())
                        intent.putExtra("password", user.hashCode().toString())
                    }
                    startActivity(intent)
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

