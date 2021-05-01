package com.example.runnerwar.api

import com.example.runnerwar.Model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @Headers("Content-Type: application/json")
    @POST("/create")
    fun newUser(
        @Body user: UserForm
    ) : Call<UserResponse>

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

    @Headers("Content-Type: application/json")
    @POST("/create/activity")
    fun createActivity(
        @Body activiy: ActivityForm
    ) : Call<Activity>


    @Headers("Content-Type: application/json")
    @GET("/create/activity")
    fun getActivity(
        @Body user: DeleteUser
    ) : Call<Activity>


    @Headers("Content-Type: application/json")
    @PUT("/create/activity")
    fun updateActivity(
        @Body activiy: Activity
    ) : Call<Codi>

}