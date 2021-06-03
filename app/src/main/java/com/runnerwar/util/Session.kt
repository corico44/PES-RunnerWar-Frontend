package com.runnerwar.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Session {

    companion object
    {

        private var id_usuario = ""
        private var username= ""

        fun loginUser(id:String, name:String){
            id_usuario = id
            username = name
        }
        private var accountname =""
        private var sessionDate = ""

        @RequiresApi(Build.VERSION_CODES.O)
        fun logIn(id:String, username : String){
            id_usuario = id
            accountname = username
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            sessionDate = current.format(formatter)
        }

        fun setSession(id: String, name : String) {
            id_usuario = id
            accountname = name
        }

        fun getIdUsuario(): String {
            return id_usuario
        }

        fun getUsername() : String{
            return username
        }

        fun setIdUsuario(id: String) {
            id_usuario = id
        }

        fun getAccountname(): String {
            return accountname
        }

        fun getCurrentDate(): String {
            return sessionDate
        }

    }
}