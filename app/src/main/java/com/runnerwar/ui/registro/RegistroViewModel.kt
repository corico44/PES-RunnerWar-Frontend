package com.runnerwar.ui.registro

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.runnerwar.Model.RegisterResponse

import com.runnerwar.Repositories.RegistroRepository
import retrofit2.Response


class RegistroViewModel(private val repository: RegistroRepository) : ViewModel() {

    private val _response= MutableLiveData<Response<RegisterResponse>>()
    val responseCreate: LiveData<Response<RegisterResponse>> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm


    fun singUpDataChanged(username: String,email: String, password: String) {
        if (!isUserNameValid(username) ) {
            _registroForm.value = RegistroFormState(usernameError = "Not a valid username" )
        } else if (!isEmailValid(email) ) {
            _registroForm.value = RegistroFormState(emailError = "Not a valid email" )
        } else if (!isPasswordValid(password)) {
            if(!contieneMinuscula(password)){
                _registroForm.value = RegistroFormState(passwordError = "Password must contain a lower case")
            } else if(!contieneMayuscula(password)){
                _registroForm.value = RegistroFormState(passwordError = "Password must contain a capital letter")
            } else {
                _registroForm.value = RegistroFormState(passwordError = "Password must be > 5 characters")
            }
        } else {
            _registroForm.value = RegistroFormState(isDataValid = true)
        }
    }
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    // A placeholder password validation check
    private fun isUserNameValid(username: String): Boolean {
        return (username.length in 5..10)
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun contieneMayuscula(a: String): Boolean {
        for(i in 0..(a.length-1)) {
            if (a[i] in 'A'..'Z') {
                return true
            }
        }
        return false
    }

    private fun contieneMinuscula(a: String): Boolean {
        for(i in 0..(a.length-1)) {
            if (a[i] in 'a'..'z') {
                return true
            }
        }
        return false
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        if(password.contains('!') or password.contains('_') or password.contains('/')){
            return false
        }
        return (password.length in 6..12 && contieneMayuscula(password) && contieneMinuscula(password))
    }

}