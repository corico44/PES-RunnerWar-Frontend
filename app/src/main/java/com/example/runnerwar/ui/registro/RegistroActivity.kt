package com.example.runnerwar.ui.registro

import android.os.Bundle
import androidx.lifecycle.Observer
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.runnerwar.R



class RegistroActivity : AppCompatActivity() {
    private lateinit var  registroViewModel: RegistroViewModel      //"lateinit" se utiliza para indicar que se inicializará más tarde, dado que no acepta un nul

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.registro)

        val username = findViewById<EditText>(R.id.reg_userName)
        val email = findViewById<EditText>(R.id.reg_email)
        val password = findViewById<EditText>(R.id.reg_password)
        val conditions = findViewById<CheckBox>(R.id.reg_checkbox)
        val signup_button = findViewById<Button>(R.id.login_button)

        registroViewModel.registroFormState.observe(this@RegistroActivity, Observer{
            val registroForm = it ?: return@Observer

            if(!registroForm.isDataValid)
                Toast.makeText(applicationContext, registroForm.usernameError, Toast.LENGTH_SHORT).show()
        })

        username.afterTextChanged {
            registroViewModel.singUpDataChanged(
                username.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        signup_button.setOnClickListener{
            registroViewModel.signUp(username.text.toString(), email.text.toString(),password.text.toString() )
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