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
    @GET("/lugar_interes")
    fun getLugaresInteres() : Call<List<LugarInteresResponse>>

    @Headers("Content-Type: application/json")
    @PUT("/points/add")
    fun updatePoints(
        @Body pointsUpdate: PointsUpdate
    ) : Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/add_friend")
    fun addFriend(
        @Body user: Friendship
    ) : Call<Codi>

    //Zonas de Confrontacion

    @Headers("Content-Type: application/json")
    @GET("/zona_confrontacion")
    fun getZonasDeConfrontacion() : Call<List<ZonaDeConfrontacion>>

    @Headers("Content-Type: application/json")
    @GET("/consult/zona_confrontacion")
    fun getZonaDeConfrontacion(
        @Query("nombre") nombreZona: String
    ) : Call<ZonaDeConfrontacion>
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
    @Headers("Content-Type: application/json")
    @POST("/update/cuenta/faction")
    fun changeFaction(
        @Body faction: FactionForm
    ) : Call<Codi>

    @Headers("Content-Type: application/json")
    @POST("/daily_login")
    fun dailyLogin(
            @Body mail: MailForm
    ) : Call<Codi>

    @Headers("Content-Type: application/json")
    @POST("/create/activity")
    fun createActivity(
        @Body activiy: ActivityForm
    ) : Call<ActivityResponse>


    @Headers("Content-Type: application/json")
    @GET("/consult/activity")
    fun getActivity(
        @Query("accountname") accountname : String, @Query("date") date: String
    ) : Call<ActivityResponse>?


    @Headers("Content-Type: application/json")
    @PUT("/update/activity")
    fun updateActivity(
        @Body activiy: ActivityUpdate
    ) : Call<ActivityResponse>


    @Headers("Content-Type: application/json")
    @GET("/users")
    fun getUsers(
    ) : Call<List<UserLeaderboards>>
}