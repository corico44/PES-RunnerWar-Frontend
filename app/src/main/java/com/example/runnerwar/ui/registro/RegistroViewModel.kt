package com.example.runnerwar.ui.registro

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.InfoUser

import com.example.runnerwar.R
import com.example.runnerwar.Repositories.RegistroRepository
import kotlinx.coroutines.launch


class RegistroViewModel(private val repository: RegistroRepository) : ViewModel() {

    val response: MutableLiveData<InfoUser> = MutableLiveData()

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm


    fun signUp(username: String, email: String, password: String) {
       viewModelScope.launch {
           val res: InfoUser = repository.newUser(username,email,password)
           response.value = res
       }
    }

    fun singUpDataChanged(username: String,email: String, password: String) {
        if (!isUserNameValid(username) ) {
            _registroForm.value = RegistroFormState(usernameError = "Not a valid username" )
        } else if (!isEmailValid(email) ) {
            _registroForm.value = RegistroFormState(emailError = "Not a valid email" )
        } else if (!isPasswordValid(password)) {
            _registroForm.value = RegistroFormState(passwordError = "Password must be >5 characters" )
        } else {
            _registroForm.value = RegistroFormState(isDataValid = true)
        }
    }

    // A placeholder password validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.length > 5
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (!email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

}