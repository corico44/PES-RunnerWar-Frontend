package com.example.runnerwar.Repositories

import com.example.runnerwar.Data.DailyActivity.ActivityDao
import com.example.runnerwar.Model.*
import com.example.runnerwar.api.RetrofitInstance
import retrofit2.Response
import retrofit2.awaitResponse

class ActivityRepository(private val activityDao: ActivityDao) {

    suspend fun newActivity(activity: ActivityForm) : Response<ActivityResponse> {
        return RetrofitInstance.api.createActivity(activity).awaitResponse()
    }

    suspend fun updateActivity(activity: ActivityUpdate) : Response<ActivityResponse> {
        return RetrofitInstance.api.updateActivity(activity).awaitResponse()
    }

    suspend fun getActivity(accountname : String , date: String) : Response<ActivityResponse>? {
        return RetrofitInstance.api.getActivity(accountname, date)?.awaitResponse()
    }


    //Calls to Local Dta Base

    //--Inserts--
    suspend fun createActivityLDB(activity: Activity){
        activityDao.createActivity(activity)
    }

    //--Gets--
    suspend fun getActivityLDB(idLoggedUser: String, date: String) : Activity {
        return activityDao.getActivity(idLoggedUser,date)
    }

    suspend fun getPointsLDB(idLoggedUser: String, date: String) : Int {
        return activityDao.getPoints(idLoggedUser,date)
    }

    suspend fun getStepsLDB(idLoggedUser: String, date: String) : Int {
        return activityDao.getSteps(idLoggedUser,date)
    }

    //--Updates--
    suspend fun updatePointsLDB(idLoggedUser: String, date: String,  points: Int){
        activityDao.updatePoints(idLoggedUser,date,points)
    }

    suspend fun updateStepsLDB(idLoggedUser: String, date: String,  steps: Int){
        activityDao.updateSteps(idLoggedUser,date,steps)
    }

    //--Deletes--
    suspend fun deleteActivityDB(idLoggedUser: String, date: String) {
        activityDao.deleteActivity(idLoggedUser,date)
    }

}