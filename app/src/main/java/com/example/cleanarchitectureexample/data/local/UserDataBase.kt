package com.example.cleanarchitectureexample.data.local

import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase

// The database class must be abstract because "Room generates the actual code during compilation"

// You are defining the schema(table) and the DAO access points, but Room provides the actual database
// implementation behind the scenes.
@Database(entities = [UserEntity::class], version = 1)
abstract class UserDataBase: RoomDatabase() {

    //Room will generate the implementation that gives you the DAO when the app runs.
    abstract fun userDao(): UserDao
}