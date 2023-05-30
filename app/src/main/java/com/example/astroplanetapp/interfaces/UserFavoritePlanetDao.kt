package com.example.astroplanetapp.interfaces

import androidx.room.Dao
import androidx.room.Insert
import com.example.astroplanetapp.models.UserFavoritePlanet

// Dao para UserFavoritePlanet
@Dao
interface UserFavoritePlanetDao {
    @Insert
    suspend fun insertUserFavoritePlanet(userFavoritePlanet: UserFavoritePlanet)

    // Otras operaciones de consulta, actualización y eliminación para UserFavoritePlanet
}