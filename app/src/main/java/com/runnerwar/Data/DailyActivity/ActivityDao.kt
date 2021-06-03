package com.runnerwar.Data.DailyActivity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.runnerwar.Model.Activity

@Dao
interface ActivityDao {

    //Inserts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createActivity(activity: Activity)

    //Gets
    @Query("SELECT * FROM activity_data WHERE _id = :idLoggedUser AND date = :date ")
    suspend fun getActivity(idLoggedUser: String, date: String) : Activity

    @Query("SELECT points FROM activity_data WHERE _id = :idLoggedUser AND date = :date ")
    suspend fun getPoints(idLoggedUser: String, date: String) : Int

    @Query("SELECT steps FROM activity_data WHERE _id = :idLoggedUser AND date = :date ")
    suspend fun getSteps(idLoggedUser: String, date: String) : Int


    // Updates
    @Query("UPDATE activity_data SET points = points + :points WHERE _id = :idLoggedUser AND date = :date ")
    suspend fun updatePoints(idLoggedUser: String, date: String,  points: Int)

    @Query("UPDATE activity_data SET points = :points WHERE _id = :idLoggedUser AND date = :date ")
    suspend fun updateAllDataPoints(idLoggedUser: String, date: String,  points: Int)

    @Query("UPDATE activity_data SET steps = :steps WHERE _id = :idLoggedUser AND date = :date ")
    suspend fun updateSteps(idLoggedUser: String, date: String,  steps: Int)


    //Deletes
    @Query("DELETE FROM activity_data WHERE _id = :idLoggedUser AND date = :date ")
    suspend fun deleteActivity(idLoggedUser: String, date: String)

}