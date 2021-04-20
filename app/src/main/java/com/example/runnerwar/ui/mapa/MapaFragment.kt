package com.example.runnerwar.ui.mapa

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapaFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapaViewModel: MapaViewModel
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private val Montjuic = LatLng(41.370950, 2.151949)
    private val ParcSantJordi = LatLng(41.3643117,2.1517766)
    private val ParcEspanyaIndustrial = LatLng(41.377597, 2.141002)
    private val ParcJoanMiro = LatLng(41.377658, 2.149280)
    private val ParcCiutadela = LatLng(41.387480, 2.186332)
    private val ParcCervantes = LatLng(41.383780, 2.106110)
    private val ParcPedralbes = LatLng(41.3876058,2.117924)
    private val ParcOreneta = LatLng(41.398790, 2.110879)
    private val ParcTuro = LatLng(41.395034, 2.140478)
    private val ParcPuxet = LatLng(41.407385, 2.142330)
    private val ParcGuell = LatLng(41.414370, 2.151912)
    private val ParcGuinardo = LatLng(41.419185, 2.167050)
    private val ParcHorta = LatLng(41.438897, 2.146656)
    private val ParcCreuetaColl = LatLng(41.418138, 2.146462)
    private val ParcTuroPeira = LatLng(41.432969, 2.164697)
    private val ParcMirador = LatLng(41.368094, 2.167112)
    private val ParcTeleferic = LatLng(41.371971, 2.172406)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapaViewModel =
            ViewModelProviders.of(this).get(MapaViewModel::class.java)
        val root = inflater.inflate(com.example.runnerwar.R.layout.fragment_mapa, container, false)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(activity!!)
        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity!!, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
             activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(activity?.application!!, currentLocation.latitude.toString() + "" +
                        currentLocation.longitude, Toast.LENGTH_SHORT).show()
                val mapFragment = childFragmentManager.findFragmentById(com.example.runnerwar.R.id.map) as SupportMapFragment?

                mapFragment!!.getMapAsync(this)
            }
        }
    }

    private fun getParks(googleMap: GoogleMap?): ArrayList<LatLng> {
        val listLugaresInteres = ArrayList<LatLng>()
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

        return listLugaresInteres
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Estoy aqui")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_down_float))
        googleMap?.addMarker(markerOptions)
        val listLugaresInteres = getParks(googleMap)
        for (loc in listLugaresInteres) {
            googleMap?.addMarker(MarkerOptions().position(loc).title("Sitio de interes"))

        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }
}