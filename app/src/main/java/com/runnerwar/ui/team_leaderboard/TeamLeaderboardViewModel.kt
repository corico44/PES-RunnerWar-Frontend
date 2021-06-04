package com.runnerwar.ui.team_leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runnerwar.Model.UserLeaderboards
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.Model.Codi
import com.runnerwar.Model.ListFactions
import com.runnerwar.Model.MailForm
import kotlinx.coroutines.launch
import retrofit2.Response

class TeamLeaderboardViewModel (private val repository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is team_leaderboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _response_daily= MutableLiveData<Codi>()
    private val _response_users= MutableLiveData<List<UserLeaderboards>>()
    private val _response_factions= MutableLiveData<ListFactions>()
    val responseDaily: LiveData<Codi> = _response_daily
    val responseUsers: LiveData<List<UserLeaderboards>> = _response_users
    val responseFactions: LiveData<ListFactions> = _response_factions


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
                    _response_daily.value = userRes!!
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
                    _response_users.value = userRes!!
                }
            }

        }
    }
    fun leaderboardsFactions() {
        viewModelScope.launch {
            val res: Response<ListFactions> = repository.getfactions()

            if (res.isSuccessful){
                val userRes : ListFactions? = res.body()

                if (userRes != null) {
                    _response_factions.value = userRes!!
                }
            }

        }
    }
}