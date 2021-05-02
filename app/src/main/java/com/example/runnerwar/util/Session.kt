package com.example.runnerwar.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Session {

    companion object{ //Acceder a estos datos sin tener una instáncia de estos
        private var id_usuario = ""
        private var accountname =""
        private lateinit var sessionDate : String

        @RequiresApi(Build.VERSION_CODES.O)
        fun logIn(id:String){
            id_usuario = id
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            sessionDate= current.format(formatter)
        }

        fun setIdUsuario(id: String) {
            id_usuario = id
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