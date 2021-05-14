package com.example.runnerwar.ui.seleccionFaccion

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Data.DailyActivity.ActivityDataBase
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import com.example.runnerwar.util.Session
import kotlinx.coroutines.launch
import retrofit2.Response
import java.security.MessageDigest

class SeleccionFaccionViewModel(private  val repository: UserRepository) : ViewModel() {

    private val _response= MutableLiveData<Codi>()
    val responseCreate: LiveData<Codi> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm

    private val _response_activity = MutableLiveData<Codi>()
    val responseActivity: LiveData<Codi> =  _response_activity


    fun signUp(user: UserForm) {
        viewModelScope.launch {
            val res: Response<RegisterResponse> = repository.newUser(user)
            var status: Codi = Codi(500)
            if (res.isSuccessful){
                val userRes : RegisterResponse? = res.body()

                if (userRes != null) {
                    status = Codi(userRes.codi!!)
                    if (status.result == 200) {
                        val user : User = User(userRes._id!!,userRes.coins!!, userRes.faction!!, userRes.password!!, userRes.points!!, userRes.accountname!!)
                        Session.setSession(user._id, user.accountname)
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

    private fun isValidUser(res: RegisterResponse) : Boolean{
        return  res.codi == 200
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
                    repositoryAct.updatePointsLDB(Session.getIdUsuario(), Session.getCurrentDate(), activity.steps / 20)
                }
            }
            _response_activity.value = Codi(200)

        }
    }

}