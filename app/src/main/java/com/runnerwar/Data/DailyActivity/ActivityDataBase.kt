package com.runnerwar.Data.DailyActivity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.runnerwar.Model.Activity


@Database(entities = [Activity::class], version = 1)
abstract class ActivityDataBase : RoomDatabase() {

    abstract  fun activityDao(): ActivityDao

    companion object {

        @Volatile
        private var INSTANCE: ActivityDataBase?= null
        fun getDataBase(context: Context): ActivityDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ActivityDataBase::class.java,
                    "activity_data"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}