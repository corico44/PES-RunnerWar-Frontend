package com.example.runnerwar.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "activity_data" , primaryKeys = ["_id", "date"])
data class Activity(

    @ColumnInfo(name = "_id") var _id: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "accountname") var accountname : String,
    @ColumnInfo(name = "steps") var steps : Int,
    @ColumnInfo(name = "points") var points : Int

)
