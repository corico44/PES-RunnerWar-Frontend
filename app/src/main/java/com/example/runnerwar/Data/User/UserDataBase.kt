package com.example.runnerwar.Data.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.runnerwar.Model.User
import com.example.runnerwar.ui.registro.RegistroViewModel
import java.security.AccessControlContext


@Database(entities = [User::class], version = 1)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile //https://www.baeldung.com/kotlin/volatile-properties -> para entender porque hacer esto

        private var INSTANCE: UserDataBase?= null

        //Mirarlo más detenidamente este bloque
        fun getDataBase(context: Context): UserDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){ // Para que solo un thread pueda ejecutar esto
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase::class.java,
                    "user_data"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }

}