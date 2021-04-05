package com.example.runnerwar.Repositories

import com.example.runnerwar.Model.*
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class RegistroRepository {


    suspend fun newUser(user: UserForm): Response<UserResponse> {
        return RetrofitInstance.api.newUser(user).awaitResponse()
    }

    suspend fun update(user: UserUpdate) : Response<UserResponse>{
        return RetrofitInstance.api.updateUser(user).awaitResponse()
    }

    suspend fun delete(user: DeleteUser) : Response<Codi>{
        return RetrofitInstance.api.deleteUser(user).awaitResponse()
    }

}