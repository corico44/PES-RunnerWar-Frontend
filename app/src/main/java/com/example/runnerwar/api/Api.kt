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
    @GET("/lugar_interes")
    fun getLugaresInteres() : Call<List<LugarInteresResponse>>


    //Zonas de Confrontacion

    @Headers("Content-Type: application/json")
    @GET("/zona_confrontacion")
    fun getZonasDeConfrontacion() : Call<List<ZonaDeConfrontacion>>

    @Headers("Content-Type: application/json")
    @GET("/consult/zona_confrontacion")
    fun getZonaDeConfrontacion(
        @Query("nombre") nombreZona: String
    ) : Call<ZonaDeConfrontacion>

}