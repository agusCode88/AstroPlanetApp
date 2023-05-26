package com.example.astroplanetapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.astroplanetapp.interfaces.PlanetDao
import com.example.astroplanetapp.models.Planet

@Database(entities = arrayOf(Planet::class), version = 1)
abstract class AstroPlanetDataBase:RoomDatabase() {

    abstract fun planetDao() : PlanetDao
    companion object{
        @Volatile
        private var INSTANCE:AstroPlanetDataBase? = null

        fun getDatabase(context: Context) : AstroPlanetDataBase? {
            val instanceDatabase = INSTANCE
            if(instanceDatabase!= null){
                return instanceDatabase
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                AstroPlanetDataBase::class.java,
                "astroPlanetDB").build()
                INSTANCE = instance

            }

            return INSTANCE
        }
    }

}