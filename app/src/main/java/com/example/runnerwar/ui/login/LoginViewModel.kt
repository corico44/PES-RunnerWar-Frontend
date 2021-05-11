package com.example.runnerwar.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import com.example.runnerwar.util.Session
import kotlinx.coroutines.launch

import retrofit2.Response
import java.security.MessageDigest


class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _response= MutableLiveData<Codi>()
    val responseCreate: LiveData<Codi> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm


    fun logIn(loginUser: LoginUser) {
        viewModelScope.launch {
            val res: Response<LoginResponse> = repository.login(loginUser)
            var status: Codi = Codi(500)

            if (res.isSuccessful){
                val userRes : LoginResponse? = res.body()

                if (userRes != null) {
                    status = Codi(userRes.codi)
                    if (status.result == 200) {
                        val user : User = User(userRes._id,userRes.coins, userRes.faction, userRes.password, userRes.points, userRes.accountname)
                        Session.loginUser(user._id, user.accountname)
                        repository.addUser(user)
                    }
                }
            }
            _response.value = status
        }
    }

    fun hashString(input: String, algorithm: String): String    {
        return MessageDigest.getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email) ) {
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
        //if (!email.contains('@')) {
        //Patterns.EMAIL_ADDRESS.matcher(email).matches()

        //} else {
        //return email.isNotBlank()
        //}
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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