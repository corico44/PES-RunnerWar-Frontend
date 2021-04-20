package com.example.runnerwar.Repositories

import androidx.lifecycle.LiveData
import com.example.runnerwar.Data.User.UserDao
import com.example.runnerwar.Model.*
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

    suspend fun login(loginUser: LoginUser): Response<User> {
        // Call to API to add new user
        return RetrofitInstance.api.login(loginUser).awaitResponse()
    }

    suspend fun update(user: UserUpdate) : Response<UserResponse> {
        return RetrofitInstance.api.updateUser(user).awaitResponse()
    }

    suspend fun deleteUser(user: DeleteUser) : Response<Codi>{
        return RetrofitInstance.api.deleteUser(user).awaitResponse()
    }


    //Calls to Local Data Base

    suspend fun addUser(user: User){
        //Add to local Data Base
        userDao.addUser(user)
    }

    suspend fun updateUser(newUsername: String){
        userDao.updateAccountName(loggedUser, newUsername)
    }

    suspend fun deleteUserFromLDB(){
        userDao.deleteUser(loggedUser)
    }

    suspend fun deleteAllDataFromLDB(){
        userDao.cleanDataBase()
    }
}