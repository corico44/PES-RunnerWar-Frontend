package com.example.runnerwar.ui.cuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.DeleteUser
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.Model.UserUpdate
import com.example.runnerwar.Repositories.RegistroRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CuentaViewModel(private val repository: RegistroRepository) : ViewModel() {

    private val _response= MutableLiveData<Response<UserResponse>>()
    val responseUpdate: LiveData<Response<UserResponse>> = _response

    private val _response_delete= MutableLiveData<Response<Codi>>()
    val responseDelete: LiveData<Response<Codi>> = _response_delete



    fun update_user(user: UserUpdate) {
        viewModelScope.launch {
            val res: Response<UserResponse> = repository.update(user)
            _response.value = res
        }
    }

    fun delete_user(user: DeleteUser) {
        viewModelScope.launch {
            val res: Response<Codi> = repository.delete(user)
            _response_delete.value = res
        }
    }


}