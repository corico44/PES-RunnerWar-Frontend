package com.example.runnerwar.Model

data class UserResponse(
    val _id: String,
    val coins: Int,
    val faction: String,
    val password: String ,
    val points: Int ,
    val accountname: String
){

    override fun toString(): String{
        val user2Str = "{_id = ${_id}, coins= ${coins}, faction = ${faction}, password = ${password}, points = ${points}, username = ${accountname}}"
        return user2Str
    }
}
