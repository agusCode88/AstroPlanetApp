package com.example.astroplanetapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad UserApp
@Entity(tableName = "users_app")
data class UserApp(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    // Otros campos de UserApp
)