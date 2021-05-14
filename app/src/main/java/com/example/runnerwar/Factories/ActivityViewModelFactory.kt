package com.example.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.NavViewModel
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.cuenta.CuentaViewModel
import com.example.runnerwar.ui.login.LoginViewModel
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel

class ActivityViewModelFactory (private val repository: ActivityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NavViewModel(repository) as T
    }
}