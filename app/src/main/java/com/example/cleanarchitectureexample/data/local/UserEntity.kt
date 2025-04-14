package com.example.cleanarchitectureexample.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val address: String,
    val company: String,
    val website: String
)
