package com.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runnerwar.NavViewModel
import com.runnerwar.Repositories.ActivityRepository

class ActivityViewModelFactory (private val repository: ActivityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NavViewModel(repository) as T
    }
}