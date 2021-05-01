package com.example.runnerwar.Services

import android.Manifest
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class ContarPasosService : IntentService("ContarPasosService"), SensorEventListener{

    private lateinit var msensorManager: SensorManager
    private lateinit var mSensor: Sensor



    init {
        instance = this
    }

    companion object{
        private  lateinit var instance: ContarPasosService

        var isRunning = false
        var pasos = 0
        val RECORD_REQUEST_CODE = 101
        private val _response= MutableLiveData<Int>()
        val responsePasos: LiveData<Int> = _response


        fun stopService(){
            isRunning= false
            instance.stopSelf()

        }
    }



    override fun onHandleIntent(intent: Intent?) {
        msensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor =  msensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        isRunning = true
        Log.d("ContarPasosService","------------------------ON----------------" )

        if (mSensor != null){
            msensorManager.registerListener(this,mSensor, SensorManager.SENSOR_DELAY_UI)
        }else {
            Toast.makeText(applicationContext, "Sensor Not Sopported", Toast.LENGTH_SHORT).show()
        }

        while (isRunning){
            //Log.d("ContarPasosService","Pasos: $pasos" )
        }


        if (!isRunning) msensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor == mSensor){
            pasos++
            _response.value = pasos
            Log.d("ContarPasosService","Pasos: $pasos" )

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }



}