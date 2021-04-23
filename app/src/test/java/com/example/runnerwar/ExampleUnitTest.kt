package com.example.runnerwar

import com.example.runnerwar.Model.LoginResponse
import com.example.runnerwar.Model.LoginUser
import com.example.runnerwar.Model.User
import com.example.runnerwar.api.RetrofitInstance
import junit.framework.Assert
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Response
import retrofit2.awaitResponse

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


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
        var api = RetrofitInstance.api
        val response : Response<LoginResponse> = api.login(loginUser).awaitResponse()
        if(response.isSuccessful){
            val res: LoginResponse? = response.body()
            val userLog : User = User(res!!._id,res!!.coins, res!!.faction, res!!.password, res!!.points, res!!.accountname)
            Assert.assertEquals(userLog, userLogged)
        }
    }


}