package com.example.runnerwar.Repositories

import com.example.runnerwar.Model.Activity
import com.example.runnerwar.Model.ActivityForm
import com.example.runnerwar.Model.Codi
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class ActivityRepository {

    suspend fun newActivity(activity: ActivityForm) : Response<Activity> {
        return RetrofitInstance.api.createActivity(activity).awaitResponse()
    }

    suspend fun updateActivity(activity: Activity) : Response<Codi> {
        return RetrofitInstance.api.updateActivity(activity).awaitResponse()
    }


}