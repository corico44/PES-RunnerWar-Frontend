package com.example.runnerwar.ui.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.R
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.hardware.SensorManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.common.api.GoogleApiClient








class MapaFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapaViewModel: MapaViewModel
    private lateinit var currentLocation: Location
    private var lastLocation: Location = Location("dummyprovider");
    private var lastMarker: Marker? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    private var mapFragment: SupportMapFragment? = null

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

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
        var locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var mGoogleApiClient: GoogleApiClient? = null
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }

        getLocationUpdates()
    }

    // A REVISAR
    private fun fetchLocation(currentLocation: Location) {

        Toast.makeText(activity?.application!!, currentLocation.latitude.toString() + "" +
                currentLocation.longitude, Toast.LENGTH_SHORT).show()
        val mapFragment = childFragmentManager.findFragmentById(com.example.runnerwar.R.id.map) as SupportMapFragment?
        println("antes del mapa")
        mapFragment!!.getMapAsync(this)


    }

    /**
     * call this method in onCreate
     * onLocationResult call when location is changed
     */
    private fun getLocationUpdates()
    {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        locationResult.lastLocation
                    // use your location object
                    // get latitude , longitude and other info from this
                    // Aquí tratar los valores para ponerlos como current y actualizar el mapa
                    if (location != null) {
                        currentLocation = location
                        fetchLocation(currentLocation)
                        println("entro")
                        // fetchLocation()
                    }
                }

            }
        }
    }

    //start location updates
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }


   override fun onMapReady(googleMap: GoogleMap?) {
       if(lastLocation != null) {
               if((!lastLocation.latitude.equals(currentLocation.latitude)) ||
                   (!lastLocation.longitude.equals(currentLocation.longitude)))
               {

                   if(lastMarker != null){
                       println("entro remove")
                       println(lastMarker)
                       lastMarker!!.remove()

                   }

               }
       }

        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_down_float))

        lastLocation = currentLocation

       lastMarker = googleMap?.addMarker(markerOptions)
    }


    /*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }*/
}