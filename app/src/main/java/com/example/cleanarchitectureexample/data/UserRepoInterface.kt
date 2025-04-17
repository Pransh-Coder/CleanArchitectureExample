package com.example.cleanarchitectureexample.data

import com.example.cleanarchitectureexample.data.remote.MappedUsersData
import com.example.cleanarchitectureexample.network.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface UserRepoInterface {

    fun getUsers() : Flow<NetworkResponse<List<MappedUsersData>>>

    fun getUserDetailsById(id: Int): Flow<NetworkResponse<MappedUsersData>>
}