package com.example.astroplanetapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Entidad UserFavoritePlanet
@Entity(
    tableName = "user_favorite_planets",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Planet::class,
            parentColumns = ["id"],
            childColumns = ["planetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["planetId"])
    ]
)
data class UserFavoritePlanet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val planetId: Long
)