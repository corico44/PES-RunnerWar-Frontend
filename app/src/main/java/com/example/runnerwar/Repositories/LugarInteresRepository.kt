package com.example.runnerwar.Repositories

import com.example.runnerwar.Data.User.UserDao
import com.example.runnerwar.Model.*
import com.example.runnerwar.api.RetrofitInstance
import com.example.runnerwar.util.Session
import retrofit2.Response
import retrofit2.awaitResponse

class LugarInteresRepository(private val userDao: UserDao){

    //Calls to API
    suspend fun getLugaresInteres(): Response<List<LugarInteresResponse>> {
        // Call to API to add new user
        println("ESTOY EN EL REPOSITORIO")
        return RetrofitInstance.api.getLugaresInteres().awaitResponse()
    }

    //Calls to API
    suspend fun updatePoints(lu: PointsUpdate): Response<LoginResponse> {
        return RetrofitInstance.api.updatePoints(lu).awaitResponse()
    }


    suspend fun updatePointsLocal(points: Int) {
        userDao.updatePoints(Session.getIdUsuario(), points)
    }


}