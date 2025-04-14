package com.example.cleanarchitectureexample

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleanarchitectureexample.data.remote.model.Address
import com.example.cleanarchitectureexample.data.remote.model.Company

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val address: Address,
    val company: Company,
    val website: String
    )
