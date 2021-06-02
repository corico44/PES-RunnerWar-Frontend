package com.example.runnerwar.ui.mapa

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Factories.LugaresViewModelFactory
import com.example.runnerwar.Model.LugarInteresResponse
import com.example.runnerwar.Model.PointsUpdate
import com.example.runnerwar.Model.ZonaDeConfrontacion
import com.example.runnerwar.Model.*
import com.example.runnerwar.Repositories.LugarInteresRepository
import com.example.runnerwar.Services.ContarPasosService
import com.example.runnerwar.util.CheckEstaDentro
import com.example.runnerwar.util.Session
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.handy.opinion.utils.LocationHelper
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.runnerwar.R
import com.example.runnerwar.Services.ContarPasosService
import com.example.runnerwar.util.Language
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.custom_dialog.*
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
    private var polygons: MutableList<Pair <String, Polygon>> = mutableListOf<Pair <String, Polygon>>()
    private var email: String? = null
    private var currentZonaConfrontacion: String? = null
    private var zonasConfrontacion: MutableList<ZonaDeConfrontacion>? = null

    private var zonaClicked : String? = null


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
            zonasConfrontacion = it as MutableList<ZonaDeConfrontacion>?
            var p1 = arrayOf(41.486502, 2.033466)
            var p2 = arrayOf(41.486465, 2.033635)
            var p3 = arrayOf(41.487851, 2.033675)
            var p4 = arrayOf(41.488387, 2.032955)

            var newZona = ZonaDeConfrontacion(
                "Rubi",
                p1, p2, p3, p4, 0, "", "", 0, 0, 0, 0
            )
            zonasConfrontacion?.add(newZona)
            añadirZonasDeConfrontacionMapa()
        })

        val root = inflater.inflate(com.example.runnerwar.R.layout.fragment_mapa, container, false)

        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val titulo_actividad: TextView =  getView()?.findViewById(R.id.titleDailyActivity) as TextView
        val titulo_puntos: TextView =  getView()?.findViewById(R.id.titlePoints) as TextView
        val titulo_pasos: TextView =  getView()?.findViewById(R.id.titleSteps) as TextView

        if(Language.idioma.equals("castellano")){
            titulo_actividad.setText("ACTIVIDAD DIARIA")
            titulo_puntos.setText("PUNTOS")
            titulo_pasos.setText("PASOS")
        }

        else if(Language.idioma.equals("ingles")){
            titulo_actividad.setText("DAILY ACTIVITY")
            titulo_puntos.setText("POINTS")
            titulo_pasos.setText("STEPS")
        }

        super.onViewCreated(view,savedInstanceState)
        LocationHelper().startListeningUserLocation(activity!!, object : LocationHelper.MyLocationListener {
            override fun onLocationChanged(location: Location) {
                // Here you got user location :)
                mLastLocation = location
                //addToMap(mLastLocation)
                if(!myList.isEmpty() && mLastLocation != null && myList != null){
                    estaDentroLugarInteres(mLastLocation,myList)
                    currentZonaConfrontacion = estaDentroZonaConfrontacion(mLastLocation)
                }
            }
        })

        infoZonaConfrontacion.visibility = View.INVISIBLE
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


        mapaViewModel.responseZCClicked.observe(viewLifecycleOwner, Observer { zona ->
            setInfoZonaDeConfrontacion(zona)
            infoZonaConfrontacion.visibility = View.VISIBLE
            if(currentZonaConfrontacion != null){
                addPoints.visibility = View.VISIBLE
            }
            else{
                addPoints.visibility = View.INVISIBLE
            }
        })


        mapaViewModel.responseGetUser.observe(viewLifecycleOwner) { user ->
            showCustomDialog(user)
        }

        mapaViewModel.responseDonate.observe(viewLifecycleOwner) {
            if (it.codi == 200) {
                if (zonaClicked != null) {
                    mapaViewModel.getZonaDeConfrontacion(zonaClicked!!)
                }
            }
        }


        addPoints.setOnClickListener {
            mapaViewModel.getUserForDonatePoints()
        }

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
            var loc = LatLng(41.488530, 2.033574)
            mMap?.addMarker(loc?.let { MarkerOptions().position(it)})
            var circleOptions = CircleOptions()
            circleOptions = circleOptions?.center(loc)?.radius(200.0)?.strokeColor(Color.BLUE)?.fillColor(0x3062BCFF)
                ?.strokeWidth(2f)!!
            var circle = mMap?.addCircle(circleOptions)
            if (circle != null) {
                myList.add(circle)
            }
        }
        if(!CheckEstaDentro.firstTimeLugarInteres) {
            CheckEstaDentro.firstTimeLugarInteres = true
            for (i in myList.indices) {
                CheckEstaDentro.estaDentroLugarInteres.add(false)
            }
        }
    }

    private fun añadirZonasDeConfrontacionMapa() {
        if(zonasConfrontacion != null){
            for(item in zonasConfrontacion!!){
                if (item._id.equals("Rubi")){
                    Log.w("Zona Confrontacion", "Mostar zona de confrontacion")
                }
                val polygonOptions = PolygonOptions().add(
                    LatLng(item.punto1[0], item.punto1[1]),
                    LatLng(item.punto2[0], item.punto2[1]),
                    LatLng(item.punto3[0], item.punto3[1]),
                    LatLng(item.punto4[0], item.punto4[1])
                ).strokeColor(Color.GRAY)
                    .fillColor(0x303C4144)
                    .strokeWidth(5.0f)
                val poly = mMap?.addPolygon(polygonOptions)
                polygons.add(Pair(item._id,poly) as Pair<String, Polygon>)
                if (item.dominant_team == "red"){
                    nameDominantFaction.setTextColor(Color.parseColor("#FA0202"))
                    if (poly != null) {
                        poly.strokeColor = Color.parseColor("#FA0202")
                        poly.fillColor = 0x30FA0202
                    }

                }
                else if (item.dominant_team == "yellow"){
                    nameDominantFaction.setTextColor(Color.parseColor("#FABE02"))
                    if (poly != null) {
                        poly.strokeColor = Color.parseColor("#FABE02")
                        poly.fillColor = 0x30FABE02
                    }
                }
                else if (item.dominant_team== "green"){
                    nameDominantFaction.setTextColor(Color.parseColor("#3B9611"))
                    if (poly != null) {
                        poly.strokeColor = Color.parseColor("#3B9611")
                        poly.fillColor = 0x303B9611
                    }
                }
                else { //Blue faction
                    nameDominantFaction.setTextColor(Color.parseColor("#0EBAB5"))
                    if (poly != null) {
                        poly.strokeColor = Color.parseColor("#0EBAB5")
                        poly.fillColor = 0x300EBAB5
                    }
                }
            }

        }
    }

    private fun estaDentroZonaConfrontacion(currentLocation: Location): String? {
        if(zonasConfrontacion != null && currentLocation != null){
            for(item in zonasConfrontacion!!){

                val pts: MutableList<LatLng> = ArrayList()
                pts.add(LatLng(item.punto1[0], item.punto1[1]))
                pts.add(LatLng(item.punto2[0], item.punto2[1]))
                pts.add(LatLng(item.punto3[0], item.punto3[1]))
                pts.add(LatLng(item.punto4[0], item.punto4[1]))

                if(currentLocation.latitude != null && currentLocation.longitude != null){
                    val contains: Boolean = PolyUtil.containsLocation(currentLocation.latitude, currentLocation.longitude, pts, true)
                    if(contains != null && contains) {
                        val text = "Esta dentro"
                        Log.w("Zona Confrontacion", "Estoy dentro de la zona de confrontacion")
                        return item._id
                    }
                }

            }
        }
        return null
    }


    fun estaDentroLugarInteres(location: Location, circles: MutableList<Circle>) {
        if(circles != null){
            for(i in circles.indices){
                if(location != null){
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
                        if(CheckEstaDentro.estaDentroLugarInteres != null && !CheckEstaDentro.estaDentroLugarInteres[i]) {
                            if(Session.getIdUsuario() != null){
                                var lu : PointsUpdate? = PointsUpdate(Session.getIdUsuario(), 100)
                                if (lu != null) {
                                    if(mapaViewModel != null) {
                                        mapaViewModel.updatePoints(lu)
                                        openPopUpDailyLogin()
                                    }
                                    //openPopUpDailyLogin()
                                }
                            }
                            CheckEstaDentro.estaDentroLugarInteres[i] = true
                        }
                    }
                }
            }
        }

    }

    fun openPopUpDailyLogin(){

        if(activity != null) {
            val dialogBuilder = AlertDialog.Builder(activity!!)
            // set message of alert dialog
            if(Language.idioma.equals("castellano")){
                dialogBuilder.setMessage("Estás en un lugar de interés")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setMessage("Felicidades, has ganado:")
                    .setMessage("+100 puntos")
                    // negative button text and action
                    .setNegativeButton("De acuerdo", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            }
            if(Language.idioma.equals("ingles")) {
                dialogBuilder.setMessage("You are in a place of interest")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setMessage("Congratulations, you earned:")
                    .setMessage("+100 points")
                    // negative button text and action
                    .setNegativeButton("Okey", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            }

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("PLACE OF INTEREST REWARD!")
            // show alert dialog
            alert.show()
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

        mMap!!.setOnMapClickListener(OnMapClickListener { point ->

            var res: String? = clickedInsideZonaConfrontacion(point)

            if (res!= null){
                zonaClicked = res
                mapaViewModel.getZonaDeConfrontacion(res)
            }
            else {
                zonaClicked = null
                infoZonaConfrontacion.visibility = View.INVISIBLE
            }
        })

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

    fun setInfoZonaDeConfrontacion(newData: ZonaConfrontacionInfo){
        nameZonaConfrontacion.text = newData._id
        nameDominantFaction.text = newData.dominant_team

        pointsYellow.text = newData.yellow_occupation.toString()
        pointsRed.text = newData.red_occupation.toString()
        pointsGreen.text = newData.green_occupation.toString()
        pointsBlue.text = newData.blue_occupation.toString()

        for(pl in polygons){
            if(pl.first.equals(newData._id)){
                if (newData.dominant_team == "red"){
                    nameDominantFaction.setTextColor(Color.parseColor("#FA0202"))
                    pl.second.strokeColor = Color.parseColor("#FA0202")
                    pl.second.fillColor = 0x30FA0202
                }
                else if (newData.dominant_team == "yellow"){
                    nameDominantFaction.setTextColor(Color.parseColor("#FABE02"))
                    pl.second.strokeColor = Color.parseColor("#FABE02")
                    pl.second.fillColor = 0x30FABE02
                }
                else if (newData.dominant_team== "green"){
                    nameDominantFaction.setTextColor(Color.parseColor("#3B9611"))
                    pl.second.strokeColor = Color.parseColor("#3B9611")
                    pl.second.fillColor = 0x303B9611
                }
                else { //Blue faction
                    nameDominantFaction.setTextColor(Color.parseColor("#0EBAB5"))
                    pl.second.strokeColor = Color.parseColor("#0EBAB5")
                    pl.second.fillColor = 0x300EBAB5
                }
            }
        }

        setDataPieChart(newData)
    }


    fun setDataPieChart(newData: ZonaConfrontacionInfo){

        //Set the values
        val oc = newData
        var entries : ArrayList<PieEntry> = ArrayList<PieEntry>()
        entries.add(PieEntry(newData.yellow_occupation.toFloat(), "Yellow Faction"))
        entries.add(PieEntry(newData.red_occupation.toFloat(), "Red Faction"))
        entries.add(PieEntry(newData.blue_occupation.toFloat(), "Blue Faction"))
        entries.add(PieEntry(newData.green_occupation.toFloat(), "Green Faction"))


        var dataSet : PieDataSet = PieDataSet(entries, "Factions")

        //Set the colors, First color -> First entry
        var colors : ArrayList<Int> = ArrayList<Int>()
        colors.add(Color.parseColor("#FABE02"))
        colors.add(Color.parseColor("#FA0202"))
        colors.add(Color.parseColor("#0EBAB5"))
        colors.add(Color.parseColor("#3B9611"))


        dataSet.colors = colors
        var data : PieData = PieData(dataSet)

        //Format of the pie chart
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(0f)
        data.setValueTextColor(Color.BLACK)
        pieChart.data = data
        pieChart.invalidate()
        pieChart.setDrawEntryLabels(false)
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false

        //Animation
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    fun clickedInsideZonaConfrontacion(location: LatLng) : String? {
        for(item in zonasConfrontacion!!) {

            val pts: MutableList<LatLng> = ArrayList()
            pts.add(LatLng(item.punto1[0], item.punto1[1]))
            pts.add(LatLng(item.punto2[0], item.punto2[1]))
            pts.add(LatLng(item.punto3[0], item.punto3[1]))
            pts.add(LatLng(item.punto4[0], item.punto4[1]))

            if (PolyUtil.containsLocation(location.latitude, location.longitude, pts, true)){
                return item._id
            }
        }

        return null
    }


    private fun showCustomDialog(user: User) {
        var dialog: Dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(com.example.runnerwar.R.layout.custom_dialog)

        var points : EditText = dialog.inputPoints
        var textDialog : TextView = dialog.textDonatePoints
        var errorText :TextView = dialog.errorText
        var donateButton : Button = dialog.confirm
        var cancelButton : Button = dialog.cancel

        textDialog.text =  "You currently have: ${user.points} pts. Please, enter the points you want to donate"

        donateButton.setOnClickListener {
            if (points.text.toString() == ""){
                errorText.text = "Enter a value"
            }
            else if (points.text.toString().toInt() > user.points){
                errorText.text ="You can't afford these points"
            }
            else {
                var donate : DonatePoints = DonatePoints(Session.getIdUsuario(),points.text.toString().toInt(), zonaClicked!! )
                dialog.dismiss()
                mapaViewModel.donatePoints(donate)
            }
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

}