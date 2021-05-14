package com.example.runnerwar.ui.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.LugaresViewModelFactory
import com.example.runnerwar.Model.LugarInteresResponse
import com.example.runnerwar.Model.PointsUpdate
import com.example.runnerwar.NavActivity
import com.example.runnerwar.Model.ZonaDeConfrontacion
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.example.runnerwar.util.CheckLugarInteres
import com.example.runnerwar.util.Session
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.handy.opinion.utils.LocationHelper
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.model.MapStyleOptions
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
import com.example.runnerwar.Services.ContarPasosService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.fragment_mapa.*








class MapaFragment : Fragment(),

    OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

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
    private var estaDentro: MutableList<Boolean> = mutableListOf<Boolean>()
    private var email: String? = null
    private var zonasConfrontacion: List<ZonaDeConfrontacion>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userDao = UserDataBase.getDataBase(activity?.application!!).userDao()
        val repository = LugarInteresRepository(userDao)
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
        LocationHelper().startListeningUserLocation(activity!!, object : LocationHelper.MyLocationListener {
            override fun onLocationChanged(location: Location) {
                // Here you got user location :)
                mLastLocation = location
                //addToMap(mLastLocation)
                if(!myList.isEmpty()){
                    estaDentro(mLastLocation,myList)
                }
            }
        })
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(activity!!)
        var mGoogleApiClient: GoogleApiClient? = null
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
        val mapFragment = childFragmentManager.findFragmentById(com.example.runnerwar.R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        ContarPasosService.dataActivity.observe(this, Observer { response ->
            pointsView.text = response.points.toString()
            stepsView.text = response.steps.toString()
        })

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
            var loc = LatLng(41.379961, 2.134193)
            mMap?.addMarker(loc?.let { MarkerOptions().position(it)})
            var circleOptions = CircleOptions()
            circleOptions = circleOptions?.center(loc)?.radius(200.0)?.strokeColor(Color.BLUE)?.fillColor(0x3062BCFF)
                ?.strokeWidth(2f)!!
            var circle = mMap?.addCircle(circleOptions)
            if (circle != null) {
                myList.add(circle)
            }
        }
        if(!CheckLugarInteres.firstTime) {
            CheckLugarInteres.firstTime = true
            for (i in myList.indices) {
                CheckLugarInteres.estaDentro.add(false)
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


    fun estaDentro(location: Location, circles: MutableList<Circle>) {
        for(i in circles.indices){
            val distance = FloatArray(2)
            val currentLatitude = location.latitude
            val currentLongitude = location.longitude
            val circleLatitude = circles[i].center.latitude
            val circleLongitude = circles[i].center.longitude

            Location.distanceBetween(
                currentLatitude, currentLongitude,
                circleLatitude, circleLongitude, distance
            )


            if (distance[0] <= circles[i]!!.radius) {
                if(!CheckLugarInteres.estaDentro[i]) {
                    var lu : PointsUpdate? = PointsUpdate(Session.getIdUsuario(), 100)
                    if (lu != null) {
                        mapaViewModel.updatePoints(lu)
                    }
                    Toast.makeText(activity!!, "Estas dentro de un lugar de interes", Toast.LENGTH_SHORT).show()
                    CheckLugarInteres.estaDentro[i] = true
                }
            }
        }

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

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(activity?.application!!,"connection failed", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(activity?.application!!,"connection suspended", Toast.LENGTH_SHORT).show()
    }

}