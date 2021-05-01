package com.example.runnerwar.util

class Session {

    companion object{ //Acceder a estos datos sin tener una instáncia de estos
        private var id_usuario = ""

        fun setIdUsuario(id: String) {
            id_usuario = id
        }

        fun getIdUsuario(): String {
            return id_usuario
        }
    }
}