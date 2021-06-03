package com.runnerwar.ui.login

import android.content.Context
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runnerwar.Data.DailyActivity.ActivityDataBase
import com.runnerwar.Repositories.ActivityRepository
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.Model.*
import com.runnerwar.ui.registro.RegistroFormState
import com.runnerwar.util.Session
import kotlinx.coroutines.launch

import retrofit2.Response
import java.security.MessageDigest


class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _response= MutableLiveData<Codi>()
    val responseCreate: LiveData<Codi> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm

    private val _response_activity = MutableLiveData<Codi>()
    val responseActivity: LiveData<Codi> =  _response_activity


    fun logIn(loginUser: LoginUser) {
        viewModelScope.launch {
            val res: Response<LoginResponse> = repository.login(loginUser)
            var status: Codi = Codi(500)

            if (res.isSuccessful){

                val userRes : LoginResponse? = res.body()

                if (userRes != null) {

                    status = Codi(userRes.codi)
                    if (status.codi == 200) {
                        val user : User = User(userRes._id,userRes.coins, userRes.faction, userRes.password, userRes.points, userRes.accountname)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Session.logIn(user._id, user.accountname)
                        }
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


    fun initServiceContarPasos( app : Context){
        val activityDao = ActivityDataBase.getDataBase(app).activityDao()
        var repositoryAct = ActivityRepository(activityDao)


        viewModelScope.launch {
            val response : Response<ActivityResponse> = repositoryAct.getActivity(Session.getAccountname(), Session.getCurrentDate())!!
            var actRes : ActivityResponse? = null
            if (response.isSuccessful){
                actRes = response.body()!!

            }

            if (actRes!!.codi == 500) { //Activity doesn't exist
                val activity = Activity(Session.getIdUsuario(), Session.getCurrentDate(), Session.getAccountname(), 0,0 )
                repositoryAct.createActivityLDB(activity)  // Create a new activity in Local
                val exists : Response<ActivityResponse> = repositoryAct.newActivity(ActivityForm(activity.accountname, activity.date))
                if (exists.isSuccessful){
                    actRes = exists.body()
                }
            }
            else { //Activity exists

                val activityDB: Activity? =
                    repositoryAct.getActivityLDB(Session.getIdUsuario(), Session.getCurrentDate())
                val activity = Activity(
                    Session.getIdUsuario(),
                    Session.getCurrentDate(),
                    Session.getAccountname(),
                    actRes.km!!,
                    actRes.km!! / 20
                )

                if (activityDB == null) { //Activity dont exist
                    repositoryAct.createActivityLDB(activity) // Create a new activity
                } else {
                    repositoryAct.updateStepsLDB(Session.getIdUsuario(), Session.getCurrentDate(), activity.steps)
                    repositoryAct.updateAllDataPointsLDB(Session.getIdUsuario(), Session.getCurrentDate(), activity.steps / 20)
                }
            }
            _response_activity.value = Codi(200)

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