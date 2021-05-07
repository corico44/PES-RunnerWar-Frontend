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
import com.example.runnerwar.Model.ZonaDeConfrontacion
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import android.R

import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.LatLng








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
    private var zonasConfrontacion: List<ZonaDeConfrontacion>? = null


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

        mapaViewModel.responseZC.observe(activity!! , Observer {
            zonasConfrontacion = it
            añadirZonasDeConfrontacionMapa()
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
        if(lugaresInteres != null){
            for(item in lugaresInteres!!){
                val position = item.latitud?.let { item.longitud?.let { it1 -> LatLng(it, it1) } }
                val descripcion = "Coordenadas: (" + item.latitud + "),(" + item.longitud + ")"
                mMap?.addMarker(position?.let { MarkerOptions().position(it).title(item._id).snippet(descripcion) })
                var circleOptions = CircleOptions()
                circleOptions = circleOptions?.center(position)?.radius(200.0)?.strokeColor(Color.BLUE)?.fillColor(0x3062BCFF)
                    ?.strokeWidth(2f)!!
                mMap?.addCircle(circleOptions)
            }
        }
    }

    private fun añadirZonasDeConfrontacionMapa() {
        if(zonasConfrontacion != null){
            for(item in zonasConfrontacion!!){

                val polygonOptions = PolygonOptions().add(
                    LatLng(item.punto1[0], item.punto1[1]),
                    LatLng(item.punto2[0], item.punto2[1]),
                    LatLng(item.punto3[0], item.punto3[1]),
                    LatLng(item.punto4[0], item.punto4[1])
                ).strokeColor(Color.GRAY)
                    .fillColor(0x303C4144)
                    .strokeWidth(5.0f)
                mMap?.addPolygon(polygonOptions)

               /* val position = item.latitud?.let { item.longitud?.let { it1 -> LatLng(it, it1) } }
                val descripcion = "Coordenadas: (" + item.latitud + "),(" + item.longitud + ")"
                mMap?.addMarker(position?.let { MarkerOptions().position(it).title(item._id).snippet(descripcion) })
                var circleOptions = CircleOptions()
                circleOptions = circleOptions?.center(position)?.radius(200.0)?.strokeColor(Color.BLUE)?.fillColor(0x3062BCFF)
                    ?.strokeWidth(2f)!!
                mMap?.addCircle(circleOptions)*/
            }
        }
    }


    fun devolverLugaresInteres(): List<LugarInteresResponse>? {
        return lugaresInteres
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

        val success = googleMap!!.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                activity, com.example.runnerwar.R.raw.style_map
            )
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.39250648830166, 2.160515736802735),13f))
        mapaViewModel.getLugaresInteres()
        mapaViewModel.getZonasDeConfrontacion()
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