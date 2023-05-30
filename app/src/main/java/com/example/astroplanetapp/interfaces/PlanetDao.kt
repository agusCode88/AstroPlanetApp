package com.example.astroplanetapp.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.astroplanetapp.models.Planet

@Dao
interface PlanetDao {

    @Query("SELECT * FROM PLANET")
    suspend fun getAllPlanets() :MutableList<Planet>

    @Insert
    suspend fun insertPlanet(planet: Planet)

    @Update
    suspend fun updatePlanet(planet:Planet)

    @Delete
    suspend fun deletePlanet(planet:Planet)

}