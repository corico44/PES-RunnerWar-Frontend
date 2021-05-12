package com.example.runnerwar.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.runnerwar.Data.User.UserDao
import com.example.runnerwar.Model.*
import com.example.runnerwar.api.RetrofitInstance
import com.example.runnerwar.util.Session
import retrofit2.Response
import retrofit2.awaitResponse

class UserRepository(private val userDao: UserDao, var loggedUser: String){

     var readAllData: LiveData<User> = userDao.readDataLoggedUser(Session.getIdUsuario())

    //Calls to API

    suspend fun newUser(user: UserForm): Response<RegisterResponse> {
        // Call to API to add new user
        return RetrofitInstance.api.newUser(user).awaitResponse()
    }

    suspend fun login(loginUser: LoginUser): Response<LoginResponse> {
        // Call to API to add new user
        return RetrofitInstance.api.login(loginUser).awaitResponse()
    }

    suspend fun update(user: UserUpdate) : Response<RegisterResponse> {
        return RetrofitInstance.api.updateUser(user).awaitResponse()
    }

    suspend fun changeFaction(faction: FactionForm) : Response<Codi> {
        return RetrofitInstance.api.changeFaction(faction).awaitResponse()
    }

    suspend fun dailyLogin(email: MailForm) : Response<Codi> {
        return RetrofitInstance.api.dailyLogin(email).awaitResponse()
    }

    suspend fun deleteUser(user: DeleteUser) : Response<Codi>{
        return RetrofitInstance.api.deleteUser(user).awaitResponse()
    }


    //Calls to Local Data Base

    suspend fun addUser(user: User){
        //Add to local Data Base
        userDao.addUser(user)
    }

    fun getUserLogged() : User{
        return userDao.getUserLogged(Session.getIdUsuario())
    }

    suspend fun updateUser(newUsername: String){
        userDao.updateAccountName(Session.getIdUsuario(), newUsername)
    }

    suspend fun updateFaction(newFaction: String){
        userDao.updateFaction(Session.getIdUsuario(), newFaction)
    }

    suspend fun deleteUserFromLDB(){
        userDao.deleteUser(Session.getIdUsuario())
    }

    suspend fun deleteAllDataFromLDB(){
        userDao.cleanDataBase()
    }
}