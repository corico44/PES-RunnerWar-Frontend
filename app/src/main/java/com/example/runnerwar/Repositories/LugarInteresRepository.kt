package com.example.runnerwar.Repositories

import androidx.lifecycle.LiveData
import com.example.runnerwar.Data.User.UserDao
import com.example.runnerwar.Model.*
import com.example.runnerwar.api.RetrofitInstance
import com.example.runnerwar.util.Session
import retrofit2.Response
import retrofit2.awaitResponse

class LugarInteresRepository(){

    //Calls to API
    suspend fun getLugaresInteres(): Response<List<LugarInteresResponse>> {
        // Call to API to add new user
        return RetrofitInstance.api.getLugaresInteres().awaitResponse()
    }

}