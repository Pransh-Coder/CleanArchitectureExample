package com.example.cleanarchitectureexample.data.remote

import com.example.cleanarchitectureexample.data.remote.model.UsersResponse
import com.example.cleanarchitectureexample.network.ApiInterface
import com.example.cleanarchitectureexample.network.Resource
import java.net.UnknownHostException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getUserListFromNetwork() : Resource<List<UsersResponse>>{

        val networkResponse = apiInterface.getUsers()
        try {
            if (networkResponse.body() != null && networkResponse.isSuccessful){
                return Resource.Success(data = networkResponse.body()!!)
            }
            else{
                return Resource.Error(errorMessage = networkResponse.errorBody().toString())
            }
        }
        catch (ex: UnknownHostException){
            return Resource.NoInternetConnection()
        }
        catch (ex:Exception){
            return Resource.Error("Exception is $ex")
        }
    }
}