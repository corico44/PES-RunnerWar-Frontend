package com.example.runnerwar.ui.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Factories.LugaresViewModelFactory
import com.example.runnerwar.Model.LugarInteresResponse
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapaFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,  LocationListener,GoogleApiClient.OnConnectionFailedListener{

    private lateinit var mapaViewModel: MapaViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback
    internal var mGoogleApiClient: GoogleApiClient? = null

    internal lateinit var mLocationRequest: LocationRequest
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mMap: GoogleMap? = null
    internal var mCurrLocationMarker: Marker? = null
    internal lateinit var mLastLocation: Location
    private var lugaresInteres: List<LugarInteresResponse>? = null
    private var myList: MutableList<Circle> = mutableListOf<Circle>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repository = LugarInteresRepository()
        val viewModelFactory = LugaresViewModelFactory(repository, 1)

        mapaViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MapaViewModel::class.java)

        mapaViewModel.responseCreate.observe(activity!! , Observer {
            lugaresInteres = it
            añadirLugaresInteresMapa()
        })

        val root = inflater.inflate(com.example.runnerwar.R.layout.fragment_mapa, container, false)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(activity!!)
        var mGoogleApiClient: GoogleApiClient? = null
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
        val mapFragment = childFragmentManager.findFragmentById(com.example.runnerwar.R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    fun añadirLugaresInteresMapa() {
        var i = 0
        if(lugaresInteres != null){
            for(item in lugaresInteres!!){
                val position = item.latitud?.let { item.longitud?.let { it1 -> LatLng(it, it1) } }
                mMap?.addMarker(position?.let { MarkerOptions().position(it).title(item._id).snippet(item.descripcion) })
                var circleOptions = CircleOptions()
                circleOptions = circleOptions?.center(position)?.radius(200.0)?.strokeColor(Color.BLUE)?.fillColor(0x3062BCFF)
                    ?.strokeWidth(2f)!!
                var circle = mMap?.addCircle(circleOptions)
                if (circle != null) {
                    myList.add(i,circle)
                }
                ++i
            }
            var loc = LatLng(41.377403, 2.147728)
            for(cir in myList){
                println("llamo x veces")
                estaDentro(loc,cir)
            }
        }
    }

    fun estaDentro(location: LatLng, circle: Circle) {
        val distance = FloatArray(2)
        val currentLatitude = location.latitude
        val currentLongitude = location.longitude
        val circleLatitude = circle.center.latitude
        val circleLongitude = circle.center.longitude

        Location.distanceBetween(
            currentLatitude, currentLongitude,
            circleLatitude, circleLongitude, distance
        )
        if (distance[0] <= circle!!.radius) {
            println("ESTOY DENTRO DEL CIRCULO " + circleLatitude + " " + circleLongitude)

        } else {
            println("ESTOY FUERA DEL CIRCULO")
        }
    }


    fun devolverLugaresInteres(): List<LugarInteresResponse>? {
        return lugaresInteres
    }


    override fun onMapReady(googleMap: GoogleMap?) {
       mMap = googleMap
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.399483, 2.169323), 15f));
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if (ContextCompat.checkSelfPermission(activity!!,
                   Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               buildGoogleApiClient()
               mMap!!.isMyLocationEnabled = true
           }
       } else {
           buildGoogleApiClient()
           mMap!!.isMyLocationEnabled = true
       }
        mapaViewModel.getLugaresInteres()
   }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(activity!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()

    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        locationCallback = LocationCallback()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper())
        }
    }

    override fun onLocationChanged(location: Location) {

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        //move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))

        //stop location updates
        if (mGoogleApiClient != null) {
            mFusedLocationClient?.removeLocationUpdates(locationCallback)
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(activity?.application!!,"connection failed", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(activity?.application!!,"connection suspended", Toast.LENGTH_SHORT).show()
    }
}