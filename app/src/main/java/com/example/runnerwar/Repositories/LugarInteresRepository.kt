package com.example.runnerwar.Repositories

import androidx.lifecycle.LiveData
import com.example.runnerwar.Data.User.UserDao
import com.example.runnerwar.Model.*
import com.example.runnerwar.api.RetrofitInstance
import com.example.runnerwar.util.Session
import io.getstream.chat.android.client.models.User
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


    suspend fun getUserInfo(): com.example.runnerwar.Model.User {
        return userDao.getUserLogged("pau.josep.ruiz@estudiantat.upc.edu")
    }


}