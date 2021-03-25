package com.example.runnerwar.ui.cuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.Model.UserUpdate
import com.example.runnerwar.Repositories.RegistroRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CuentaViewModel(private val repository: RegistroRepository) : ViewModel() {

    private val _response= MutableLiveData<Response<UserResponse>>()
    val responseUpdate: LiveData<Response<UserResponse>> = _response




    fun update_user(user: UserUpdate) {
        viewModelScope.launch {
            val res: Response<UserResponse> = repository.update(user)
            _response.value = res
        }
    }



}