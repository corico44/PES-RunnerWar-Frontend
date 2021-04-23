package com.example.runnerwar.ui.registro

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.InfoUser
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse

import com.example.runnerwar.R
import com.example.runnerwar.Repositories.RegistroRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class RegistroViewModel(private val repository: RegistroRepository) : ViewModel() {

    private val _response= MutableLiveData<Response<UserResponse>>()
    val responseCreate: LiveData<Response<UserResponse>> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm


    fun signUp(user: UserForm) {
       viewModelScope.launch {
           val res: Response<UserResponse> = repository.newUser(user)
           _response.value = res
       }
    }

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