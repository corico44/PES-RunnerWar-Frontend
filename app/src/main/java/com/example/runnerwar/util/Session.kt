package com.example.runnerwar.util

class Session {

    companion object{ //Acceder a estos datos sin tener una instáncia de estos
        private var id_usuario = ""
        private var username= ""

        fun loginUser(id:String, name:String){
            id_usuario = id
            username = name
        }

        fun setIdUsuario(id: String) {
            id_usuario = id
        }

        fun getIdUsuario(): String {
            return id_usuario
        }

        fun getUsername() : String{
            return username
        }
    }
}