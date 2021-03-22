package com.example.runnerwar.api

import com.example.runnerwar.Model.InfoUser
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @POST("path")
    suspend fun newUser(@Path("username") username : String, @Path("username") email : String, @Path("password") password : String) : InfoUser

}