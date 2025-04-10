package com.example.cleanarchitectureexample.network

import com.example.cleanarchitectureexample.data.remote.model.UsersData
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("users")
    suspend fun getUsers(): Response<List<UsersData>>
}