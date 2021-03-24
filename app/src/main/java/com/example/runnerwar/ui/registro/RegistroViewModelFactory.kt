package com.example.runnerwar.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.Repositories.RegistroRepository

class RegistroViewModelFactory (private val repository: RegistroRepository, private val value: Int) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(value == 1) {
            return RegistroViewModel(repository) as T
        } else {
            return SeleccionFaccionViewModel(repository) as T
        }
    }

}