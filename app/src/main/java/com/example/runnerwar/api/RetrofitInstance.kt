package com.example.runnerwar.api

import com.example.runnerwar.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Se hace esto para hacer Retrofit singleton
object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://enigmatic-sea-32759.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : Api by lazy {
        retrofit.create(Api::class.java)
    }

}