package com.example.runnerwar

import com.example.runnerwar.Model.LoginUser
import com.example.runnerwar.Model.User
import com.example.runnerwar.api.RetrofitInstance
import junit.framework.Assert.assertEquals
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse

class LoginApiUnitTest {

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
    suspend fun correct_login(){


        var loginUser: LoginUser = LoginUser("example@default.com", "12345")
        var userLogged : User = User("example@default.com", 0, "red", "12345", 0, "example" )

        val response : Response<User> = api.login(loginUser).awaitResponse()
        if(response.isSuccessful){
            val res: User? = response.body()
            assertEquals(res, userLogged)
        }
    }

    @Test
    suspend fun error_login_email(){
        var loginUser: LoginUser = LoginUser("exampleError@default.com", "12345")
        val response : Response<User> = api.login(loginUser).awaitResponse()
        if(response.isSuccessful){
            val res: User? = response.body()
            assertEquals(res, null)
        }

    }

    @Test
    suspend fun error_login_password(){
        var loginUser: LoginUser = LoginUser("example@default.com", "12345678")
        val response : Response<User> = api.login(loginUser).awaitResponse()
        if(response.isSuccessful){
            val res: User? = response.body()
            assertEquals(res, null)
        }

    }

    @Test
    suspend fun error_login_email_password(){
        var loginUser: LoginUser = LoginUser("exampleERrror@default.com", "12345678")
        val response : Response<User> = api.login(loginUser).awaitResponse()
        if(response.isSuccessful){
            val res: User? = response.body()
            assertEquals(res, null)
        }
    }

}