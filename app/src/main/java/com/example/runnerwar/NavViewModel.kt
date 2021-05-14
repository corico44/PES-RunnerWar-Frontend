package com.example.runnerwar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.Activity
import com.example.runnerwar.Model.ActivityForm
import com.example.runnerwar.Model.ActivityResponse
import com.example.runnerwar.Model.ActivityUpdate
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.Services.ContarPasosService
import com.example.runnerwar.util.Session
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NavViewModel (private var repository: ActivityRepository) : ViewModel(){

    private lateinit var date: String

    fun updateActivityData() {
        viewModelScope.launch {
            var act :Activity = repository.getActivityLDB(Session.getIdUsuario(), Session.getCurrentDate())
            repository.updateActivity(ActivityUpdate(act.accountname, act.date, act.steps))
        }

    }

}