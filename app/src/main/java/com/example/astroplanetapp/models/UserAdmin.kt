package com.example.astroplanetapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_admin")
data class UserAdmin(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    // Otros campos de UserAdmin
)