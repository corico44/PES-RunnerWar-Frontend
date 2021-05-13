package com.example.runnerwar.Repositories

import com.example.runnerwar.Model.ZonaDeConfrontacion
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class ZonasDeConfrontacionRepository {


    suspend fun getZonasDeConfrontacion(): Response<List<ZonaDeConfrontacion>>{
        return RetrofitInstance.api.getZonasDeConfrontacion().awaitResponse()
    }

    suspend fun getZonaDeConfrontacion( nombreZona : String) : Response<ZonaDeConfrontacion>{
        return RetrofitInstance.api.getZonaDeConfrontacion(nombreZona).awaitResponse()
    }

}