package com.example.runnerwar.ui.mapa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import com.example.runnerwar.util.Session
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class MapaViewModel(private val repository: LugarInteresRepository) : ViewModel() {

    private var _response = MutableLiveData<List<LugarInteresResponse>>()
    var responseCreate: LiveData<List<LugarInteresResponse>> = _response

   fun getLugaresInteres() {
        viewModelScope.launch {
            val res: Response<List<LugarInteresResponse>> = repository.getLugaresInteres()

            if (res.isSuccessful){
                val userRes : List<LugarInteresResponse>? = res.body()

                if (userRes != null) {
                    _response.value = userRes
                }
            }
        }
    }


    fun updatePoints(lu: PointsUpdate) {
        viewModelScope.launch {
            val res: Response<LoginResponse> = repository.updatePoints(lu)

        }
    }
}