package com.example.runnerwar.ui.buscarCuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.UserRepository
import kotlinx.coroutines.launch

import retrofit2.Response
import java.security.MessageDigest


class SearchViewModel(private val repository: UserRepository) : ViewModel() {

    private val _response= MutableLiveData<SearchResponse>()
    val userSearched: LiveData<SearchResponse> = _response

    fun searchUser(searchUser: SearchUser) {
        viewModelScope.launch {
            val res: Response<SearchResponse> = repository.searchUser(searchUser)

            if (res.isSuccessful){
                val userRes : SearchResponse? = res.body()

                if (userRes != null) {
                    _response.value = userRes
                }
            }

        }
    }
}