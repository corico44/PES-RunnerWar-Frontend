package com.example.runnerwar.Repositories

import com.example.runnerwar.Model.InfoUser
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class RegistroRepository {

    suspend fun newUser(user: UserForm): Response<UserResponse> {
        return RetrofitInstance.api.newUser(user).awaitResponse()
    }

}