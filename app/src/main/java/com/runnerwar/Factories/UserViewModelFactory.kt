package com.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.ui.buscarCuenta.SearchViewModel
import com.runnerwar.ui.user_leaderboard.UserLeaderboardViewModel
import com.runnerwar.ui.cambiarFaccion.CambiarFaccionViewModel
import com.runnerwar.ui.cuenta.CuentaViewModel
import com.runnerwar.ui.team_leaderboard.TeamLeaderboardViewModel
import com.runnerwar.ui.login.LoginViewModel
import com.runnerwar.ui.seleccionFaccion.SeleccionFaccionViewModel


class UserViewModelFactory(private val repository: UserRepository, private val numView : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if ( numView ==1){
            return SeleccionFaccionViewModel(repository) as T
        }
        else if(numView == 2){
            return CuentaViewModel(repository) as T
        }
        else if(numView == 3){
            return SearchViewModel(repository) as T
        }
        else if(numView == 4){
            return CambiarFaccionViewModel(repository) as T
        }
        else if(numView == 5){
            return TeamLeaderboardViewModel(repository) as T
        }
        else if(numView == 6){
            return LoginViewModel(repository) as T
        }
        else {
            return UserLeaderboardViewModel(repository) as T
        }
    }
}