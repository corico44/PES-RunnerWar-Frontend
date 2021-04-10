package com.example.runnerwar.Model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_data")
data class User(
    @PrimaryKey val _id: String,
    val coins: Int,
    val faction: String,
    val password: String,
    val points: Int,
    val accountname: String
)
