package com.example.cleanarchitectureexample.domain

import com.example.cleanarchitectureexample.data.UserRepoInterface
import com.example.cleanarchitectureexample.data.local.LocalDataSource
import com.example.cleanarchitectureexample.network.Resource
import com.example.cleanarchitectureexample.presentation.UsersData
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetUsersDataUseCase @Inject constructor(
    private val repository: UserRepoInterface,
    private val localDataSource: LocalDataSource,
) {

    /*operator fun invoke() = repository.getUsers().transform {
        val sortedListByName = it.data?.sortedBy { it.name }
        when(it){
            is Resource.Loading -> {
                emit(Resource.Loading())
            }
            is Resource.Success -> {
                emit(Resource.Success(sortedListByName))
            }
            is Resource.Error ->{
                emit(Resource.Error(it.errorMessage))
            }
        }
    }

    operator fun invoke(id: Int) = repository.getUserDetailsById(id)*/

    suspend operator fun invoke(): Resource<List<UsersData>> {
        val networkResponse = repository.getUsersFromNetwork()
        when(networkResponse){
            is Resource.Success -> {
                val usersDataFromNetwork = networkResponse.data.map {
                    UsersData(
                        id = it.id,
                        name = it.name,
                        email = it.email,
                        phone = it.phone,
                        address = "${it.address.suite} ${it.address.street}, ${it.address.city}, ${it.address.zipcode}",
                        company = "${it.company.name} | ${it.company.catchPhrase}"
                    )
                }
                localDataSource.insertAllUsersInDB(usersList = networkResponse.data)

                return Resource.Success(data = usersDataFromNetwork)
            }
            is Resource.NoInternetConnection -> {
                return Resource.NoInternetConnection()
            }
            is Resource.Error -> {
                return Resource.Error(networkResponse.errorMessage)
            }
        }
    }


    companion object{
        private const val TAG = "GetUsersDataUseCase"
    }
}