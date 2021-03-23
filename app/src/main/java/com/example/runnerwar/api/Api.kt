package com.example.runnerwar.api

import com.example.runnerwar.Model.InfoUser
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @Headers("Content-Type: application/json")
    @POST("/create")
    fun newUser(
        @Body user: UserForm
    ) : Call<UserResponse>

}