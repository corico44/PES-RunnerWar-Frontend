package com.example.runnerwar

import com.example.runnerwar.Model.LugarInteresResponse
import com.example.runnerwar.ui.mapa.MapaFragment
import com.google.android.gms.maps.model.LatLng
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MapaUnitTest {

    var resultLugaresInteres = MapaFragment()
    private var Montjuic = LatLng(41.370951, 2.151949)
    private var ParcSantJordi = LatLng(41.364311, 2.151776)
    private var ParcEspanyaIndustrial = LatLng(41.377597, 2.141002)
    private var ParcJoanMiro = LatLng(41.377658, 2.149281)
    private var ParcCiutadela = LatLng(41.387481, 2.186332)
    private var ParcCervantes = LatLng(41.383781, 2.106111)
    private var ParcPedralbes = LatLng(41.387606, 2.117924)
    private var ParcOreneta = LatLng(41.398791, 2.110879)
    private var ParcTuro = LatLng(41.395034, 2.140478)
    private var ParcPuxet = LatLng(41.407385, 2.142331)
    private var ParcGuell = LatLng(41.414371, 2.151912)
    private var ParcGuinardo = LatLng(41.419185, 2.167051)
    private var ParcHorta = LatLng(41.438897, 2.146656)
    private var ParcCreuetaColl = LatLng(41.418138, 2.146462)
    private var ParcTuroPeira = LatLng(41.432969, 2.164697)
    private var ParcMirador = LatLng(41.368094, 2.167112)
    private var ParcTeleferic = LatLng(41.371971, 2.172406)

   /* @Test
    fun first_place_is_correct() {
        lugaresInteres = resultLugaresInteres.devolverLugaresInteres()!!
        var coordenadas = lugaresInteres!![0].longitud?.let { lugaresInteres!![0].latitud?.let { it1 -> LatLng(it1, it) } }
        assertEquals(Montjuic, coordenadas)
        var listLugaresInteres = ArrayList<LatLng>()
        //listLugaresInteres = resultLugaresInteres.getParks()
        assertEquals(Montjuic, listLugaresInteres[0])
    }

    @Test
    fun size_decimals_longitude() {
        var listLugaresInteres = ArrayList<LatLng>()
        var resultArray: ArrayList<LatLng>

        listLugaresInteres.add(Montjuic)
        listLugaresInteres.add(ParcSantJordi)
        listLugaresInteres.add(ParcEspanyaIndustrial)
        listLugaresInteres.add(ParcJoanMiro)
        listLugaresInteres.add(ParcCiutadela)
        listLugaresInteres.add(ParcCervantes)
        listLugaresInteres.add(ParcPedralbes)
        listLugaresInteres.add(ParcOreneta)
        listLugaresInteres.add(ParcTuro)
        listLugaresInteres.add(ParcPuxet)
        listLugaresInteres.add(ParcGuell)
        listLugaresInteres.add(ParcGuinardo)
        listLugaresInteres.add(ParcHorta)
        listLugaresInteres.add(ParcCreuetaColl)
        listLugaresInteres.add(ParcTuroPeira)
        listLugaresInteres.add(ParcMirador)
        listLugaresInteres.add(ParcTeleferic)

        resultArray = resultLugaresInteres.getParks()
        resultArray = resultLugaresInteres.getParks()

        var i = 0
        var size : Array<String>
        for(loc in resultArray){
            size = listLugaresInteres[i].longitude.toString().split('.').toTypedArray()
            assertEquals(6, size[1].length)
            ++i
        }

    }

    @Test
    fun size_decimals_latitude() {
        var listLugaresInteres = ArrayList<LatLng>()
        var resultArray: ArrayList<LatLng>

        listLugaresInteres.add(Montjuic)
        listLugaresInteres.add(ParcSantJordi)
        listLugaresInteres.add(ParcEspanyaIndustrial)
        listLugaresInteres.add(ParcJoanMiro)
        listLugaresInteres.add(ParcCiutadela)
        listLugaresInteres.add(ParcCervantes)
        listLugaresInteres.add(ParcPedralbes)
        listLugaresInteres.add(ParcOreneta)
        listLugaresInteres.add(ParcTuro)
        listLugaresInteres.add(ParcPuxet)
        listLugaresInteres.add(ParcGuell)
        listLugaresInteres.add(ParcGuinardo)
        listLugaresInteres.add(ParcHorta)
        listLugaresInteres.add(ParcCreuetaColl)
        listLugaresInteres.add(ParcTuroPeira)
        listLugaresInteres.add(ParcMirador)
        listLugaresInteres.add(ParcTeleferic)

       resultArray = resultLugaresInteres.getParks()
       resultArray = resultLugaresInteres.getParks()

        var i = 0
        var size : Array<String>
        for(loc in resultArray){
            size = listLugaresInteres[i].latitude.toString().split('.').toTypedArray()
            assertEquals(6, size[1].length)
            ++i
        }

    }

    @Test
    fun all_places_are_correct() {
        var listLugaresInteres = ArrayList<LatLng>()
        var resultArray: ArrayList<LatLng>

        listLugaresInteres.add(Montjuic)
        listLugaresInteres.add(ParcSantJordi)
        listLugaresInteres.add(ParcEspanyaIndustrial)
        listLugaresInteres.add(ParcJoanMiro)
        listLugaresInteres.add(ParcCiutadela)
        listLugaresInteres.add(ParcCervantes)
        listLugaresInteres.add(ParcPedralbes)
        listLugaresInteres.add(ParcOreneta)
        listLugaresInteres.add(ParcTuro)
        listLugaresInteres.add(ParcPuxet)
        listLugaresInteres.add(ParcGuell)
        listLugaresInteres.add(ParcGuinardo)
        listLugaresInteres.add(ParcHorta)
        listLugaresInteres.add(ParcCreuetaColl)
        listLugaresInteres.add(ParcTuroPeira)
        listLugaresInteres.add(ParcMirador)
        listLugaresInteres.add(ParcTeleferic)

        resultArray = resultLugaresInteres.getParks()

        var i = 0
        for(loc in resultArray){
            assertEquals(listLugaresInteres[i],loc)
            ++i
        }*/

    }