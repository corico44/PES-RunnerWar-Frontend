package com.example.runnerwar.ui.mapa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is mapa Fragment"
    }
    val text: LiveData<String> = _text
}