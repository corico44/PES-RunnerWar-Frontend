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
import android.os.Build
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.common.api.GoogleApiClient


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
        var mGoogleApiClient: GoogleApiClient? = null
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
        val mapFragment = childFragmentManager.findFragmentById(com.example.runnerwar.R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

   override fun onMapReady(googleMap: GoogleMap?) {
       mMap = googleMap
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