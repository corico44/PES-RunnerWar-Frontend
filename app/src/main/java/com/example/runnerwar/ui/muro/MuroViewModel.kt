package com.example.runnerwar.ui.muro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MuroViewModel (private val repository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is muro Fragment"
    }
    val text: LiveData<String> = _text

    private val _response_daily= MutableLiveData<Codi>()
    private val _response_users= MutableLiveData<List<UserLeaderboards>>()
    val responseDaily: LiveData<Codi> = _response_daily
    val responseUsers: LiveData<List<UserLeaderboards>> = _response_users


    fun dailyLogin(mail : MailForm){
        viewModelScope.launch {
            val res: Response<Codi> = repository.dailyLogin(mail)

            if (res.isSuccessful){
                val userRes : Codi? = res.body()

                if (userRes != null) {
                    if(userRes.codi == 200){
                        repository.updatePoints(20)
                    }
                }


                if (userRes != null) {
                    _response_daily.value = userRes
                }
            }
        }
    }

    fun leaderboardsUsers() {
        viewModelScope.launch {
            val res: Response<List<UserLeaderboards>> = repository.getallusers()

            if (res.isSuccessful){
                val userRes : List<UserLeaderboards>? = res.body()

                if (userRes != null) {
                    _response_users.value = userRes
                }
            }

        }
    }
}