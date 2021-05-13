package com.example.runnerwar.ui.seleccionFaccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.RegisterResponse
import com.example.runnerwar.Model.User
import com.example.runnerwar.Model.UserForm
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.registro.RegistroFormState
import com.example.runnerwar.util.Session
import kotlinx.coroutines.launch
import retrofit2.Response
import java.security.MessageDigest

class SeleccionFaccionViewModel(private  val repository: UserRepository) : ViewModel() {

    private val _response= MutableLiveData<Codi>()
    val responseCreate: LiveData<Codi> = _response

    private val _registroForm = MutableLiveData<RegistroFormState>()
    val registroFormState: LiveData<RegistroFormState> = _registroForm


    fun signUp(user: UserForm) {
        viewModelScope.launch {
            val res: Response<RegisterResponse> = repository.newUser(user)
            var status: Codi = Codi(500)
            if (res.isSuccessful){
                val userRes : RegisterResponse? = res.body()

                if (userRes != null) {
                    status = Codi(userRes.codi!!)
                    if (status.codi == 200) {
                        val user : User = User(userRes._id!!,userRes.coins!!, userRes.faction!!, userRes.password!!, userRes.points!!, userRes.accountname!!)
                        Session.setIdUsuario(user._id)
                        repository.addUser(user)
                    }
                }
            }
            _response.value = status
        }
    }

    fun hashString(input: String, algorithm: String): String    {
        return MessageDigest.getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun isValidUser(res: RegisterResponse) : Boolean{
        return  res.codi == 200
    }
}