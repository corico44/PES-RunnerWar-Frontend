package com.example.runnerwar.ui.mapa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.example.runnerwar.Repositories.ZonasDeConfrontacionRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MapaViewModel(private val repositoryLI: LugarInteresRepository, private val repositoryZC : ZonasDeConfrontacionRepository) : ViewModel() {

    private var _response= MutableLiveData<List<LugarInteresResponse>>()
    var responseCreate: LiveData<List<LugarInteresResponse>> = _response

    private var _responseZC = MutableLiveData<List<ZonaDeConfrontacion>>()
    var responseZC: LiveData<List<ZonaDeConfrontacion>> = _responseZC

   fun getLugaresInteres() {
        viewModelScope.launch {
            val res: Response<List<LugarInteresResponse>> = repositoryLI.getLugaresInteres()

            if (res.isSuccessful){
                val userRes : List<LugarInteresResponse>? = res.body()

                if (userRes != null) {
                    _response.value = userRes
                }
            }
        }
    }

    fun getZonasDeConfrontacion(){
        viewModelScope.launch {
            val res: Response<List<ZonaDeConfrontacion>> = repositoryZC.getZonasDeConfrontacion()

            if (res.isSuccessful){
                val zc : List<ZonaDeConfrontacion>? = res.body()

                if (zc != null) {
                    _responseZC.value = zc
                }
            }
        }
    }


    fun updatePoints(lu: PointsUpdate) {
        viewModelScope.launch {
            val res: Response<LoginResponse> = repositoryLI.updatePoints(lu)

            if(res.isSuccessful){
                println("ACTUALIZO LOCAL")
               // repositoryLI.updatePointsLocal(lu.points)
            }
        }
    }
}