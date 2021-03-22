package com.example.runnerwar.Repositories

import com.example.runnerwar.Model.InfoUser
import com.example.runnerwar.api.RetrofitInstance

class RegistroRepository {

    suspend fun newUser(username: String, email :String, password :String): InfoUser{
        return RetrofitInstance.api.newUser(username, email, password)
    }

}