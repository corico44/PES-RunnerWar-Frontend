package com.example.runnerwar.ui.muro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.MailForm
import com.example.runnerwar.Repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MuroViewModel (private val repository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is muro Fragment"
    }
    val text: LiveData<String> = _text

    private val _response_daily= MutableLiveData<Response<Codi>>()
    val responseDaily: LiveData<Response<Codi>> = _response_daily


    fun dailyLogin(mail : MailForm){
        viewModelScope.launch {
            val res: Response<Codi> = repository.dailyLogin(mail)

            _response_daily.value = res
        }
    }
}