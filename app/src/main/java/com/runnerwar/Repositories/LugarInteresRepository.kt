package com.runnerwar.Repositories

import com.runnerwar.Data.User.UserDao
import com.runnerwar.Model.LoginResponse
import com.runnerwar.Model.LugarInteresResponse
import com.runnerwar.Model.PointsUpdate
import com.runnerwar.api.RetrofitInstance
import com.runnerwar.util.Session
import retrofit2.Response
import retrofit2.awaitResponse

class LugarInteresRepository(private val userDao: UserDao){

    //Calls to API
    suspend fun getLugaresInteres(): Response<List<LugarInteresResponse>> {
        // Call to API to add new user
        return RetrofitInstance.api.getLugaresInteres().awaitResponse()
    }

    //Calls to API
    suspend fun updatePoints(lu: PointsUpdate): Response<LoginResponse> {
        return RetrofitInstance.api.updatePoints(lu).awaitResponse()
    }


    suspend fun updatePointsLocal(points: Int) {
        println("REPO LOCAL")
        userDao.updatePoints(Session.getIdUsuario(), points)
    }


    suspend fun getUserInfo(): com.runnerwar.Model.User {
        return userDao.getUserLogged(Session.getIdUsuario())
    }


}