package com.runnerwar.ui.mapa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runnerwar.Repositories.LugarInteresRepository
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.Repositories.ZonasDeConfrontacionRepository
import com.runnerwar.Model.*
import kotlinx.coroutines.launch
import retrofit2.Response

class MapaViewModel(private val repositoryLI: LugarInteresRepository, private val repositoryZC : ZonasDeConfrontacionRepository, private val repositoryU: UserRepository) : ViewModel() {



    private var _response= MutableLiveData<List<LugarInteresResponse>>()
    var responseCreate: LiveData<List<LugarInteresResponse>> = _response

    private var _responseZC = MutableLiveData<List<ZonaDeConfrontacion>>()
    var responseZC: LiveData<List<ZonaDeConfrontacion>> = _responseZC

    private var _responseZCClicked = MutableLiveData<ZonaConfrontacionInfo>()
    var responseZCClicked: LiveData<ZonaConfrontacionInfo> = _responseZCClicked

    private var _responseGetUser = MutableLiveData<com.runnerwar.Model.User>()
    var responseGetUser: LiveData<com.runnerwar.Model.User> = _responseGetUser

    private var _responseDonate = MutableLiveData<Codi>()
    var responseDonate: LiveData<Codi> = _responseDonate

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

   fun getZonaDeConfrontacion(name :String){
       viewModelScope.launch {
           val res: Response<ZonaConfrontacionInfo> = repositoryZC.getZonaDeConfrontacion(name)
            if (res.isSuccessful){
               val zc : ZonaConfrontacionInfo? = res.body()
                if (zc != null) {
                   _responseZCClicked.value = zc!!
               }
           }
       }
   }

    fun getUserForDonatePoints()  {
        viewModelScope.launch {
            Log.w("MOSTRAR-DIALOGO", com.runnerwar.util.Session.getIdUsuario())
            //var user : com.runnerwar.User = repositoryLI.getUserInfo()
            //Log.w("MOSTRAR-DIALOGO", com.runnerwar.Session.getIdUsuario())
            _responseGetUser.value = repositoryLI.getUserInfo()
            //Log.w("MOSTRAR-DIALOGO", "He entrado aqui")*/
        }



    }


    fun updatePoints(lu: PointsUpdate) {
        viewModelScope.launch {
            val res: Response<LoginResponse> = repositoryLI.updatePoints(lu)

            if(res.isSuccessful){
                repositoryLI.updatePointsLocal(lu.points)
            }
        }
    }

    fun donatePoints(points : DonatePoints){
        viewModelScope.launch {
            var res: Response<Codi> = repositoryZC.donatePoints(points)

            if(res.isSuccessful){
                val coinsToAdd = points.points/10
                val coins = Coins(com.runnerwar.util.Session.getIdUsuario(), coinsToAdd)
                res = repositoryU.addCoins(coins)
                if(res.isSuccessful){
                    repositoryU.addCoinsLDB(coinsToAdd)
                    repositoryLI.updatePointsLocal(-points.points)
                    _responseDonate.value = res.body()
                }
            }
        }
    }
}
