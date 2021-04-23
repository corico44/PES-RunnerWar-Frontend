package com.example.runnerwar

import com.example.runnerwar.Model.RegisterResponse
import com.example.runnerwar.Model.User
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.api.RetrofitInstance
import junit.framework.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import retrofit2.awaitResponse

class RegisterApiUniTest {

    var api = RetrofitInstance.api

    /*

    Test para comprobar las llamadas a la Api relacionadas con el Register

    Para hacer el registro a nuestra app, utilizaremos los siguientes datos:

    emai: example@default.com
    accountname: example
    password: 12345
    faction: red

    */

    var email:String =  "example@default.com"
    var accountname: String = "example"
    var password:String =  "12345"
    var faction: String = "red"


    @Test
    suspend fun correct_register(){
        var userForm: UserForm = UserForm(email, accountname, password, faction)
        var userCorrect : User = User("example@default.com", 0, "red", "12345", 0, "example" )

        val response : Response<RegisterResponse> = api.newUser(userForm).awaitResponse()
        if(response.isSuccessful){
            val res: RegisterResponse? = response.body()
            var user: User = User(res!!._id, res!!.coins, res!!.faction, res!!.password, res!!.points, res!!.accountname)
            assertEquals(res.codi, 200)
            assertEquals(res, userCorrect)
        }
    }


    @Test
    suspend fun register_user_exists(){
        var userForm:UserForm = UserForm(email, accountname, password, faction)

        val response : Response<RegisterResponse> = api.newUser(userForm).awaitResponse()
        if(response.isSuccessful){
            val res: RegisterResponse? = response.body()
            if (res != null) {
                assertEquals(res.codi, 500)
                assertEquals(res._id, null)
                assertEquals(res.accountname, null)
                assertEquals(res.password, null)
                assertEquals(res.points, null)
                assertEquals(res.faction, null)
                assertEquals(res.coins, null)
            }

        }
    }

    @Test
    suspend fun register_username_exists(){
        var userForm:UserForm = UserForm("example99@default.com", accountname, password, faction)

        val response : Response<RegisterResponse> = api.newUser(userForm).awaitResponse()
        if(response.isSuccessful){
            val res: RegisterResponse? = response.body()
            if (res != null) {
                assertEquals(res.codi, 500)
                assertEquals(res._id, null)
                assertEquals(res.accountname, null)
                assertEquals(res.password, null)
                assertEquals(res.points, null)
                assertEquals(res.faction, null)
                assertEquals(res.coins, null)
            }

        }
    }


}