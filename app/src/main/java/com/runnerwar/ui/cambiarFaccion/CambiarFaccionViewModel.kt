package com.runnerwar.ui.cambiarFaccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.Model.Codi
import com.runnerwar.Model.FactionForm
import com.runnerwar.Model.RegisterResponse
import com.runnerwar.Model.User
import kotlinx.coroutines.launch
import retrofit2.Response

class CambiarFaccionViewModel (private  val repository: UserRepository) : ViewModel(){

    val readAllData: LiveData<User>
    private val _response= MutableLiveData<Response<Codi>>()
    val responseChangeFaction: LiveData<Response<Codi>> = _response

    init {
        readAllData = repository.readAllData
    }

    fun changeFaction(faction: FactionForm) {
        viewModelScope.launch {
            val res: Response<Codi> = repository.changeFaction(faction)
            if (res.isSuccessful) {
                //Update local dataBase
               repository.updateFaction(faction.faction)
            }
            _response.value = res
        }
    }




    private fun isValidUser(res: RegisterResponse) : Boolean{
        return  res.codi == 200
    }
}