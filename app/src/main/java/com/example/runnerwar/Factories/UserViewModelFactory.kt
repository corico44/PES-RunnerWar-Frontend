package com.example.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.cambiarFaccion.CambiarFaccionViewModel
import com.example.runnerwar.ui.cuenta.CuentaViewModel
import com.example.runnerwar.ui.muro.MuroViewModel
import com.example.runnerwar.ui.login.LoginViewModel
import com.example.runnerwar.ui.registro.RegistroViewModel
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel


class UserViewModelFactory(private val repository: UserRepository, private val numView : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( numView ==1){
            return SeleccionFaccionViewModel(repository) as T
        }

        else if(numView == 2){
            return CuentaViewModel(repository) as T
        }
        else if(numView == 3){
            return LoginViewModel(repository) as T
        }
        else if(numView == 4){
            return CambiarFaccionViewModel(repository) as T
        }
        else if(numView == 5){
            return MuroViewModel(repository) as T
        }
        else {
            return LoginViewModel(repository) as T
        }
    }
}