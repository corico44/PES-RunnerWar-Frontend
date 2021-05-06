package com.example.runnerwar.Model

import androidx.room.ColumnInfo

data class SearchResponse(
    val _id: String,
    val coins: Int,
    val faction: String,
    val points: Int,
    val accountname: String,
    val codi: Int
)
