package com.example.runnerwar.Model

data class ZonaDeConfrontacion(
    var _id : String,
    var punto1: String,
    var punto2: String,
    var punto3: String,
    var punto4: String,
    var puntuacion: Int,
    var descripcion : String,
    var dominant_team: String,
    var red_ocupation: Int,
    var blue_ocupation: Int,
    var yellow_ocupation: Int,
    var green_ocupation: Int
)
