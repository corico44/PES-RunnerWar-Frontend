package com.example.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.ui.cuenta.CuentaViewModel
import com.example.runnerwar.ui.login.LoginViewModel
import com.example.runnerwar.ui.mapa.MapaViewModel
import com.example.runnerwar.ui.registro.RegistroViewModel
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel

class LugaresViewModelFactory(private val repository: LugarInteresRepository, private val numView : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( numView == 1){
            return MapaViewModel(repository) as T
        }
        else{
            return null as T
        }
    }
}