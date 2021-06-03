package com.runnerwar.Model

import androidx.room.ColumnInfo

data class LoginResponse(
    val _id: String,
    val coins: Int,
    val faction: String,
    val password: String,
    val points: Int,
    val accountname: String,
    val codi: Int
)
