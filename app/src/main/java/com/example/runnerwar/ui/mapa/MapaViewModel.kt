package com.example.runnerwar.ui.mapa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.LoginResponse
import com.example.runnerwar.Model.LugarInteresResponse
import com.example.runnerwar.Model.User
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import com.example.runnerwar.util.Session
import retrofit2.Response

class MapaViewModel(private val repository: LugarInteresRepository) : ViewModel() {

    private val _response = MutableLiveData<LugarInteresResponse>()
    val responseCreate: LiveData<LugarInteresResponse> = _response

    suspend fun getLugaresInteres(): List<LugarInteresResponse>? {

        val res: Response<List<LugarInteresResponse>> = repository.getLugaresInteres()
        if (res.isSuccessful){
            val userRes : List<LugarInteresResponse>? = res.body()
            return userRes
        }
        return emptyList()
    }
}