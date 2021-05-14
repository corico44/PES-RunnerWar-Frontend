package com.example.runnerwar.ui.cuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CuentaViewModel(private val repository: UserRepository) : ViewModel() {

    val readAllData: LiveData<User>

    private val _response= MutableLiveData<Response<RegisterResponse>>()
    val responseUpdate: LiveData<Response<RegisterResponse>> = _response

    private val _response_delete= MutableLiveData<Response<Codi>>()
    val responseDelete: LiveData<Response<Codi>> = _response_delete

    init {
        readAllData = repository.readAllData
    }



    fun updateUser(user: UserUpdate) {
        viewModelScope.launch {
            val res: Response<RegisterResponse> = repository.update(user)
            val data: RegisterResponse? = res.body()

            if (res.isSuccessful){
                //Update local dataBase
                if (data != null) {
                    if (data.codi == 200)
                        repository.updateUser(user.accountname)
                }
            }
            _response.value = res
        }
    }

   fun deleteUser(user: DeleteUser) {
        viewModelScope.launch {
            val res: Response<Codi> = repository.deleteUser(user)

            if (res.isSuccessful){
                //Update local dataBase
                repository.deleteUserFromLDB()
            }

            _response_delete.value = res
        }
    }



}