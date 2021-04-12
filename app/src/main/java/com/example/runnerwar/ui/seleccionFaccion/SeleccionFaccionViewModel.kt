package com.example.runnerwar.ui.seleccionFaccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Model.User
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Model.UserResponse
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import kotlinx.coroutines.launch
import retrofit2.Response

class SeleccionFaccionViewModel(private  val repository: UserRepository) : ViewModel() {

    private val _response= MutableLiveData<Response<User>>()
    val responseCreate: LiveData<Response<User>> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm


    fun signUp(user: UserForm) {
        viewModelScope.launch {
            val res: Response<User> = repository.newUser(user)

            if (res.isSuccessful){
                val user : User? = res.body()

                if (user != null) {
                    //repository.deleteUser()
                    repository.addUser(user)
                }

            }
            _response.value = res
        }
    }
}