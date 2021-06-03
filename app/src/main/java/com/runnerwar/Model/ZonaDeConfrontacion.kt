package com.runnerwar.Model

data class ZonaDeConfrontacion(
    var _id : String,
    var punto1: Array<Double>,
    var punto2: Array<Double>,
    var punto3: Array<Double>,
    var punto4:  Array<Double>,
    var puntuacion: Int,
    var descripcion : String,
    var dominant_team: String,
    var red_ocupation: Int,
    var blue_ocupation: Int,
    var yellow_ocupation: Int,
    var green_ocupation: Int
)
