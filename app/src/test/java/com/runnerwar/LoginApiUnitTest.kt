package com.runnerwar

import com.runnerwar.Model.LoginResponse
import com.runnerwar.Model.LoginUser
import com.runnerwar.Model.User
import com.runnerwar.api.RetrofitInstance
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking

import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import retrofit2.awaitResponse


class LoginApiUnitTest {

    //var api = RetrofitInstance.api
        var api = RetrofitInstance.api

    /*

        Test para comprobar las llamadas a la Api relacionadas con el Login

        Antes de hacer el login debe haberse hecho un registro con los siguientes datos:
        emai: example@default.com
        accountname: example
        password: 12345
        faction: red
     */

    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }


    @Test
    fun correct_login(){
        var loginUser: LoginUser = LoginUser("example@default.com", "12345")
        var userLogged : User = User("example@default.com", 0, "red", "12345", 0, "example" )


        val response : Response<LoginResponse> = runBlocking { api.login(loginUser).awaitResponse() }
        if(response.isSuccessful){
            val res: LoginResponse? = response.body()
            val userLog : User = User(res!!._id,res!!.coins, res!!.faction, res!!.password, res!!.points, res!!.accountname)
            assertEquals(userLog, userLogged)
        }
    }

    @Test
    fun error_login_email(){
        var loginUser: LoginUser = LoginUser("exampleError@default.com", "12345")
        val response : Response<LoginResponse> = runBlocking {  api.login(loginUser).awaitResponse()}
        if(response.isSuccessful){
            val res: LoginResponse? = response.body()
            assertEquals(res!!.codi, 500)
        }

    }

    @Test
    fun error_login_password(){
        var loginUser: LoginUser = LoginUser("example@default.com", "12345678")
        val response : Response<LoginResponse> = runBlocking { api.login(loginUser).awaitResponse()}
        if(response.isSuccessful){
            val res: LoginResponse? = response.body()
            assertEquals(res!!.codi, 500)
        }

    }

    @Test
    fun error_login_email_password(){
        var loginUser: LoginUser = LoginUser("exampleERrror@default.com", "12345678")
        val response : Response<LoginResponse> = runBlocking {  api.login(loginUser).awaitResponse()}
        if(response.isSuccessful){
            val res: LoginResponse? = response.body()
            assertEquals(res!!.codi, 500)
        }
    }

    @Test
    fun get_activity(){
        var loginUser: LoginUser = LoginUser("PauRu99", "20210122")
        //val response : Response<ActivityResponse> = runBlocking {  api.getActivity("PauRu99", "20210122").awaitResponse()}
        /*if(response.isSuccessful){
            val res: ActivityResponse? = response.body()
            assertEquals(res!!.codi, 500)
        }*/
    }
}