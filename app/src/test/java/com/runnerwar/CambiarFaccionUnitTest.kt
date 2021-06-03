package com.runnerwar

import com.runnerwar.api.RetrofitInstance
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response
import retrofit2.awaitResponse
import com.runnerwar.Model.FactionForm
import com.runnerwar.Model.Codi

import org.junit.Assert.assertEquals

class CambiarFaccionUnitTest {

        var api = RetrofitInstance.api

    /*

    Test para comprobar las llamadas a la Api relacionadas con el Cambio de faccion

    Antes de hacer el cambio de faccion debe haberse hecho un registro con los siguientes datos:
    emai: test@default.com
    accountname: test1234
    password: Pass12345
    faction: red
 */
    @Test
    fun correct_change_faction(){
        var faction : FactionForm = FactionForm("test@default.com","yellow")
        //var userLogged : User = User("test@default.com", 0, "red", "Pass12345", 0, "test1234" )

        val response : Response<Codi> = runBlocking{api.changeFaction(faction).awaitResponse()}
        if(response.isSuccessful){
            assertEquals(200, response.body()?.codi)
        }

    }
}