package com.example.runnerwar.Repositories

import androidx.lifecycle.LiveData
import com.example.runnerwar.Data.User.UserDao
import com.example.runnerwar.Model.User
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class UserRepository(private val userDao: UserDao){

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun newUser(user: UserForm): Response<User> {
        return RetrofitInstance.api.newUser(user).awaitResponse()
    }

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    /*suspend fun deleteUser(){
        userDao.deleteUser()
    }*/
}