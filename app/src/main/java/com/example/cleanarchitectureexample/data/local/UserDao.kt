package com.example.cleanarchitectureexample.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("Select * from users")
    suspend fun getAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(userEntities: List<UserEntity>)

    @Delete
    suspend fun deleteAllUsers(userEntities: List<UserEntity>)

    @Query("Select * from users where id = :id")
    suspend fun getUserDataById(id: Int): UserEntity

}