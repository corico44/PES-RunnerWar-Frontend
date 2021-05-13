package com.example.runnerwar.api

import com.example.runnerwar.Model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @Headers("Content-Type: application/json")
    @POST("/create")
    fun newUser(
        @Body user: UserForm
    ) : Call<RegisterResponse>

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(
        @Body loginUser: LoginUser
    ) : Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @PUT("/update_accountname")
    fun updateUser(
        @Body user: UserUpdate
    ) : Call<RegisterResponse>

    @Headers("Content-Type: application/json")
    @POST("/delete")
    fun deleteUser(
        @Body user: DeleteUser
    ) : Call<Codi>

    @Headers("Content-Type: application/json")
    @POST("/consult/other/account")
    fun searchUser(
        @Body searchUser: SearchUser
    ) : Call<SearchResponse>

    @Headers("Content-Type: application/json")
    @POST("/add_friend")
    fun addFriend(
        @Body user: Friendship
    ) : Call<Codi>

    @Headers("Content-Type: application/json")
    @POST("/delete_friend")
    fun deleteFriend(
        @Body user: Friendship
    ) : Call<Codi>

    @Headers("Content-Type: application/json")
    @POST("/search_friend")
    fun searchFriend(
        @Body user: Friendship
    ) : Call<Codi>

}