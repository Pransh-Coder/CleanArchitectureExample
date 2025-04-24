package com.example.cleanarchitectureexample.domain

import android.util.Log
import com.example.cleanarchitectureexample.data.UserRepoInterface
import com.example.cleanarchitectureexample.network.Resource
import com.example.cleanarchitectureexample.presentation.UsersData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUsersLocalDataUseCase @Inject constructor(
    private val repoInterface: UserRepoInterface,
) {

    operator fun invoke() : Flow<Resource<List<UsersData>>> {
        return repoInterface.getUsersFromDataBase().map {
            when (it) {
                is Resource.Success -> {
                    val convertedUserData = it.data.map {
                        UsersData(
                            id = it.id,
                            name = it.name,
                            email = it.email,
                            phone = it.phone,
                            address = it.address,
                            company = it.company
                        )
                    }
                    Resource.Success(data = convertedUserData)
                }

                is Resource.NoInternetConnection -> {
                    Resource.NoInternetConnection()
                }

                is Resource.Error -> {
                    Resource.Error(errorMessage = it.errorMessage.toString())
                }
            }
        }
    }
}