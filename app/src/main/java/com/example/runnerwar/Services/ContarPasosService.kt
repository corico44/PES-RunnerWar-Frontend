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
import com.example.runnerwar.Data.User.UserDataBase
import com.example.runnerwar.Model.Activity
import com.example.runnerwar.Model.LoginResponse
import com.example.runnerwar.Model.PointsUpdate
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.Repositories.UserRepository
import com.example.runnerwar.util.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ContarPasosService : IntentService("ContarPasosService"), SensorEventListener{

    private lateinit var msensorManager: SensorManager
    private lateinit var mSensor: Sensor

    init {
        instance = this
        val activityDao = ActivityDataBase.getDataBase(context).activityDao()
        repositoryAct = ActivityRepository(activityDao)

        val userDao = UserDataBase.getDataBase(context).userDao()
        repositoryUser = UserRepository(userDao, Session.getIdUsuario())

    }


    companion object{
        private  lateinit var instance: ContarPasosService
        private lateinit var date: String
        private lateinit var activity : Activity
        lateinit var context: Context
        private  lateinit var  repositoryAct : ActivityRepository
        private  lateinit var  repositoryUser : UserRepository
        var isRunning = false


        private val _response= MutableLiveData<Activity>()
        val dataActivity: LiveData<Activity> = _response

        fun getActivitySession() : Activity{
            return activity!!
        }


    }


    override fun onHandleIntent(intent: Intent?) {
        msensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor =  msensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        activity = runBlocking { repositoryAct.getActivityLDB(Session.getIdUsuario(), Session.getCurrentDate()) }

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
            activity.steps++
            runBlocking { repositoryAct.updateStepsLDB(Session.getIdUsuario(), Session.getCurrentDate(), activity!!.steps) }
            if (activity.steps%20 == 0){
                activity.points = activity.steps/20
                runBlocking { repositoryAct.updatePointsLDB(Session.getIdUsuario(), Session.getCurrentDate(), 1)}
                runBlocking { repositoryUser.updatePointsLDB(Session.getIdUsuario(),/*activity!!.points*/ 1) }
                CoroutineScope(Dispatchers.IO).launch {
                    repositoryUser.updatePoints(PointsUpdate( Session.getIdUsuario(),/*activity!!.points*/1))
                }
            }
            _response.value = activity
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}