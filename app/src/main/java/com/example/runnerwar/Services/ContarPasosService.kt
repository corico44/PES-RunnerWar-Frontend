package com.example.runnerwar.Services


import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runnerwar.Data.DailyActivity.ActivityDataBase
import com.example.runnerwar.Model.Activity
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.util.Session
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ContarPasosService : IntentService("ContarPasosService"), SensorEventListener{

    private lateinit var msensorManager: SensorManager
    private lateinit var mSensor: Sensor
    private val repository : ActivityRepository



    init {
        instance = this
        val activityDao = ActivityDataBase.getDataBase(context).activityDao()
        repository = ActivityRepository(activityDao)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setDate()
        }

        activity = existActivity()

        if (activity == null) { //Activity dont exist
            activity = Activity(Session.getIdUsuario(), date, Session.getAccountname(), 0,0 )
            runBlocking { repository.createActivityLDB(activity!!) } // Create a new activity
        }
    }


    companion object{
        private  lateinit var instance: ContarPasosService
        private lateinit var date: String
        private var activity : Activity? = null
        lateinit var context: Context
        var isRunning = false


        private val _response= MutableLiveData<Activity>()
        val dataActivity: LiveData<Activity> = _response
        /*fun stopService(){
            isRunning= false
            instance.stopSelf()

        }*/
    }



    override fun onHandleIntent(intent: Intent?) {
        msensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor =  msensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        isRunning = true

        if (mSensor != null){
            msensorManager.registerListener(this,mSensor, SensorManager.SENSOR_DELAY_UI)
        }else {
            Toast.makeText(applicationContext, "Sensor Not Sopported", Toast.LENGTH_SHORT).show()
        }

        if (!isRunning) msensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event!!.sensor == mSensor){
            activity!!.steps++
            runBlocking { repository.updateStepsLDB(Session.getIdUsuario(), date, activity!!.steps) }
            if (activity!!.steps%20 == 0){
                activity!!.points = activity!!.steps/20
                runBlocking { repository.updatePointsLDB(Session.getIdUsuario(), date, activity!!.points) }
            }
            _response.value = activity
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun existActivity(): Activity {
        return runBlocking { repository.getActivityLDB(Session.getIdUsuario(), date) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDate() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        date = current.format(formatter)
    }

}