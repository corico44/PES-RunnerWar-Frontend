package com.runnerwar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runnerwar.Model.Activity
import com.runnerwar.Model.ActivityUpdate
import com.runnerwar.Repositories.ActivityRepository
import com.runnerwar.util.Session
import kotlinx.coroutines.launch

class NavViewModel (private var repository: ActivityRepository) : ViewModel(){

    private lateinit var date: String

    fun updateActivityData() {
        viewModelScope.launch {
            var act : Activity = repository.getActivityLDB(Session.getIdUsuario(), Session.getCurrentDate())
            repository.updateActivity(ActivityUpdate(act.accountname, act.date, act.steps))
        }

    }

}