package com.runnerwar.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runnerwar.Repositories.RegistroRepository

class RegistroViewModelFactory (private val repository : RegistroRepository, private val value: Int) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistroViewModel(repository) as T
    }

}