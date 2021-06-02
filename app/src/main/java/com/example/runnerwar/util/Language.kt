package com.example.runnerwar.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.runnerwar.Data.DailyActivity.ActivityDataBase
import com.example.runnerwar.Model.Activity
import com.example.runnerwar.Model.ActivityForm
import com.example.runnerwar.Model.ActivityResponse
import com.example.runnerwar.Repositories.ActivityRepository
import com.example.runnerwar.Services.ContarPasosService
import com.example.runnerwar.Services.ContarPasosService.Companion.context
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Language {

    companion object
    {

        var idioma = "none"

    }
}