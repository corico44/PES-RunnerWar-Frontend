package com.example.runnerwar.ui.seleccionFaccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import kotlinx.coroutines.launch
import retrofit2.Response

class SeleccionFaccionViewModel(private  val repository: RegistroRepository) : ViewModel() {

    private val _response= MutableLiveData<Response<UserResponse>>()
    val responseCreate: LiveData<Response<UserResponse>> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm

    fun signUp(user: UserForm) {
        viewModelScope.launch {
            val res: Response<UserResponse> = repository.newUser(user)
            _response.value = res
        }
    }
}