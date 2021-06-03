package com.runnerwar.ui.calendario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runnerwar.Model.UserLeaderboards
import com.runnerwar.Repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CalendarioViewModel(private val repository: UserRepository) : ViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "This is chat Fragment"
    }
    val text: LiveData<String> = _text

    private val _response_users= MutableLiveData<List<UserLeaderboards>>()
    val responseUsers: LiveData<List<UserLeaderboards>> = _response_users

    fun leaderboardsUsers() {
        viewModelScope.launch {
            val res: Response<List<UserLeaderboards>> = repository.getallusers()

            if (res.isSuccessful){
                val userRes : List<UserLeaderboards>? = res.body()

                if (userRes != null) {
                    _response_users.value = userRes!!
                }
            }

        }
    }
}