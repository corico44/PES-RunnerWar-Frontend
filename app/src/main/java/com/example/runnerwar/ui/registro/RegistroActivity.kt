package com.example.runnerwar.ui.registro

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

import com.example.runnerwar.R

class RegistroActivity : AppCompatActivity() {
    private lateinit var  registroViewModel: RegistroViewModel      //"lateinit" se utiliza para indicar que se inicializará más tarde, dado que no acepta un nul

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.registro)

        val username = findViewById<EditText>(R.id.reg_userName)
        val email = findViewById<EditText>(R.id.reg_email)
        val password = findViewById<EditText>(R.id.reg_password)
        val conditions = findViewById<CheckBox>(R.id.reg_checkbox)
        val login_button = findViewById<Button>(R.id.login_button)

        /*login_button.setOnClickListener{
            registroViewModel.login(username.text.toString(), email.text.toString(), )
        }*/


    }
}