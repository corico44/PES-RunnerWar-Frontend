package com.example.runnerwar.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_data")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val _id: String,

    @ColumnInfo(name = "coins")
    val coins: Int,

    @ColumnInfo(name = "faction")
    val faction: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "points")
    val points: Int,

    @ColumnInfo(name = "accountname")
    val accountname: String
)
