package com.example.astroplanetapp.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.astroplanetapp.models.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user:User):Long

//    @Query("SELECT * FROM user WHERE id=:idUser")
//    suspend fun getUserById(idUser: Long):User

    @Delete
    suspend fun deleteUser(user: User)

}