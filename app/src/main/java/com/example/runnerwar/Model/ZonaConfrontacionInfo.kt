package com.example.runnerwar.Model

data class ZonaConfrontacionInfo(
    var _id : String,
    var blue_occupation: Int,
    var codi : String,
    var descripcion : String,
    var dominant_team: String,
    var green_occupation: Int,
    var punto1: Array<Double>,
    var punto2: Array<Double>,
    var punto3: Array<Double>,
    var punto4:  Array<Double>,
    var puntuacion: Int,
    var red_occupation: Int,
    var yellow_occupation: Int
)
