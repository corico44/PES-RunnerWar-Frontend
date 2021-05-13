package com.example.runnerwar.ui.buscarCuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class SearchViewModel(private val repository: UserRepository) : ViewModel() {

    private val _response= MutableLiveData<SearchResponse>()
    val userSearched: LiveData<SearchResponse> = _response

    private val _response_search= MutableLiveData<Response<Codi>>()
    val responseSearch: LiveData<Response<Codi>> = _response_search

    private val _response_add_friend= MutableLiveData<Response<Codi>>()
    val responseAddFriend: LiveData<Response<Codi>> = _response_add_friend

    private val _response_delete_friend= MutableLiveData<Response<Codi>>()
    val responseDeleteFriend: LiveData<Response<Codi>> = _response_delete_friend

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

    fun addFriend(user: Friendship) {
        viewModelScope.launch {
            val res: Response<Codi> = repository.addFriend(user)

            _response_add_friend.value = res
        }
    }

    fun deleteFriend(user: Friendship) {
        viewModelScope.launch {
            val res: Response<Codi> = repository.deleteFriend(user)

            _response_delete_friend.value = res
        }
    }

    fun searchFriend(user: Friendship) {
        viewModelScope.launch {
            val res: Response<Codi> = repository.searchFriend(user)

            _response_search.value = res
        }
    }
}