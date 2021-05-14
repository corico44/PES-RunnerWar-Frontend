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

    companion object
    {

        private var id_usuario = ""
        private var accountname =""
        private lateinit var sessionDate : String

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

    }
}