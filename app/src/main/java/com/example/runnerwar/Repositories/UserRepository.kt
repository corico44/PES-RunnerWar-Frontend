package com.example.runnerwar.Repositories

import androidx.lifecycle.LiveData
import com.example.runnerwar.Data.User.UserDao
import com.example.runnerwar.Model.User
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.Model.UserUpdate
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class UserRepository(private val userDao: UserDao, private val loggedUser: String){

     var readAllData: LiveData<User> = userDao.readDataLoggedUser(loggedUser)

    //Calls to API

    suspend fun newUser(user: UserForm): Response<User> {
        // Call to API to add new user
        return RetrofitInstance.api.newUser(user).awaitResponse()
    }

    suspend fun update(user: UserUpdate) : Response<UserResponse> {
        return RetrofitInstance.api.updateUser(user).awaitResponse()
    }


    //Calls to Local Data Base

    suspend fun addUser(user: User){
        //Add to local Data Base
        userDao.addUser(user)
    }

    fun updateUser(newUsername: String){
        userDao.updateAccountName(loggedUser, newUsername)
    }
}