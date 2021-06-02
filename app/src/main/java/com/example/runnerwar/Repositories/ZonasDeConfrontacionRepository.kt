package com.example.runnerwar.Repositories

import com.example.runnerwar.Model.Codi
import com.example.runnerwar.Model.DonatePoints
import com.example.runnerwar.Model.ZonaConfrontacionInfo
import com.example.runnerwar.Model.ZonaDeConfrontacion
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class ZonasDeConfrontacionRepository {


    suspend fun getZonasDeConfrontacion(): Response<List<ZonaDeConfrontacion>>{
        return RetrofitInstance.api.getZonasDeConfrontacion().awaitResponse()
    }

    suspend fun getZonaDeConfrontacion( nombreZona : String) : Response<ZonaConfrontacionInfo>{
        return RetrofitInstance.api.getZonaDeConfrontacion(nombreZona).awaitResponse()
    }

    suspend fun donatePoints( points: DonatePoints) : Response<Codi>{
        return RetrofitInstance.api.donatePointsToZC(points).awaitResponse()
    }
}