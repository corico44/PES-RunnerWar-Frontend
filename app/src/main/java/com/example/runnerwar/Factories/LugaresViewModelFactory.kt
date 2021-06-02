package com.example.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.Repositories.ZonasDeConfrontacionRepository
import com.example.runnerwar.ui.cuenta.CuentaViewModel
import com.example.runnerwar.ui.login.LoginViewModel
import com.example.runnerwar.ui.mapa.MapaViewModel
import com.example.runnerwar.ui.registro.RegistroViewModel
import com.example.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel
import com.example.runnerwar.util.Session

class LugaresViewModelFactory(private val repository: LugarInteresRepository,private val repositoryU: UserRepository , private val numView : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( numView == 1){
            val repositoryZC = ZonasDeConfrontacionRepository()
            return MapaViewModel(repository, repositoryZC, repositoryU) as T
        }
        else{
            return null as T
        }
    }
}