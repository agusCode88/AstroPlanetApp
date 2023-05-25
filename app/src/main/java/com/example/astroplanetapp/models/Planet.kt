package com.example.astroplanetapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Planet")
data class Planet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var nombre: String,
    var imagen: String = "",
    var superficie: Double = 0.0,
    var isFavorite: Boolean = false
)
