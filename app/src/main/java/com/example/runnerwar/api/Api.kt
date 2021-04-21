package com.example.runnerwar.api

import com.example.runnerwar.Model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @Headers("Content-Type: application/json")
    @POST("/create")
    fun newUser(
        @Body user: UserForm
    ) : Call<User>

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(
        @Body loginUser: LoginUser
    ) : Call<User>

    @Headers("Content-Type: application/json")
    @PUT("/update_accountname")
    fun updateUser(
        @Body user: UserUpdate
    ) : Call<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("/delete")
    fun deleteUser(
        @Body user: DeleteUser
    ) : Call<Codi>




}