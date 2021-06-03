package com.runnerwar.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.runnerwar.Repositories.LugarInteresRepository
import com.runnerwar.Repositories.UserRepository
import com.runnerwar.Repositories.ZonasDeConfrontacionRepository
import com.runnerwar.ui.mapa.MapaViewModel

class LugaresViewModelFactory(private val repository: LugarInteresRepository, private val repositoryU: UserRepository, private val numView : Int) : ViewModelProvider.Factory {
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