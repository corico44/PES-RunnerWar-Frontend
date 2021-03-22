package com.example.runnerwar.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.Repositories.RegistroRepository

class RegistroViewModelFactory (private val repository: RegistroRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistroViewModel(repository) as T
    }

}