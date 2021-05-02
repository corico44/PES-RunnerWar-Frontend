package com.example.runnerwar.ui.cambiarFaccion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import com.example.runnerwar.util.Session
import kotlinx.coroutines.launch
import retrofit2.Response
import java.security.MessageDigest

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