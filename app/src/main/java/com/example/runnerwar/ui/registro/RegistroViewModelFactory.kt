package com.example.runnerwar.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.Repositories.RegistroRepository
import com.example.runnerwar.ui.cuenta.CuentaViewModel
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel

class RegistroViewModelFactory (private val repository : RegistroRepository, private val value: Int) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistroViewModel(repository) as T
    }

}