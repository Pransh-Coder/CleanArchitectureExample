package com.example.cleanarchitectureexample

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cleanarchitectureexample.data.remote.model.UsersData

@Dao
interface UserDao {

    @Query("Select * from users")
    suspend fun getAllUsers(): List<UsersData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<UsersData>)
}