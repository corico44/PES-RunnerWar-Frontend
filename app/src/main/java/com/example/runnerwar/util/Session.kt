package com.example.runnerwar.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.runnerwar.Data.DailyActivity.ActivityDataBase
import com.example.runnerwar.Model.Activity
import com.example.runnerwar.Model.ActivityForm
import com.example.runnerwar.Model.ActivityResponse
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.Services.ContarPasosService
import com.example.runnerwar.Services.ContarPasosService.Companion.context
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Session {

    companion object{ //Acceder a estos datos sin tener una instáncia de estos
        private var id_usuario = ""
        private var accountname =""
        private lateinit var sessionDate : String
        private var activity : Activity? = null
        private  lateinit var  repository : ActivityRepository


        @RequiresApi(Build.VERSION_CODES.O)
        fun logIn(id:String, username : String){
            id_usuario = id
            accountname = username
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            sessionDate= current.format(formatter)



        }

        fun setSession(id: String, name : String) {
            id_usuario = id
            accountname = name
        }

        fun getIdUsuario(): String {
            return id_usuario
        }

        fun getAccountname(): String {
            return accountname
        }

        fun getCurrentDate(): String {
            return sessionDate
        }



        fun initServiceContarPasos( app : Context){

            val activityDao = ActivityDataBase.getDataBase(app).activityDao()
            repository = ActivityRepository(activityDao)

            runBlocking{

                var actRes = existActivity()

              /*  if (actRes!!.codi == 500) { //Activity doesn't exist
                    activity = Activity(getIdUsuario(), sessionDate, getAccountname(), 0,0 )
                    repository.createActivityLDB(activity!!)  // Create a new activity in Local
                    repository.newActivity(ActivityForm(activity!!.accountname, activity!!.date))
                }
                else { //Activity exists

                    var activityDB: Activity?= repository.getActivityLDB(getIdUsuario(), sessionDate)
                    activity = Activity(getIdUsuario(), sessionDate, getAccountname(), actRes.km!!, actRes.km!! /20 )

                    if (activityDB == null) { //Activity dont exist
                        repository.createActivityLDB(activity!!) // Create a new activity
                    }
                    else{
                        repository.updateStepsLDB(getIdUsuario(), sessionDate, activity!!.steps)
                        repository.updatePointsLDB(getIdUsuario(), sessionDate, activity!!.steps/20)
                    }
                }*/

            }

        }



        private suspend fun existActivity(): ActivityResponse? {
            var response : Response<ActivityResponse> = repository.getActivity(accountname, sessionDate)!!
            var actRes : ActivityResponse? = null
            if (response!!.isSuccessful){
                actRes = response.body()!!
            }

            return  actRes
        }





    }
}