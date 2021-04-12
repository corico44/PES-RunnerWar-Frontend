package com.example.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.cuenta.CuentaViewModel
import com.example.runnerwar.ui.registro.RegistroViewModel
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel

class UserViewModelFactory(private val repository: UserRepository, private val numView : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( numView ==1){
            return SeleccionFaccionViewModel(repository) as T
        }

        else {
            return CuentaViewModel(repository) as T
        }
    }
}