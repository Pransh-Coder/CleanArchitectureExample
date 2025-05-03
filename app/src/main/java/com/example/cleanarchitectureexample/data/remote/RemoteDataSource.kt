package com.example.cleanarchitectureexample.data.remote

import com.example.cleanarchitectureexample.data.remote.model.UsersResponse
import com.example.cleanarchitectureexample.network.ApiInterface
import com.example.cleanarchitectureexample.network.Resource
import java.net.UnknownHostException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getUserListFromNetwork() : Resource<List<UsersResponse>>{

        val networkResponse = apiInterface.getUsers()
        return try {
            if (networkResponse.body() != null && networkResponse.isSuccessful){
                Resource.Success(data = networkResponse.body()!!)
            }
            else{
                Resource.Error(errorMessage = networkResponse.errorBody().toString())
            }
        }
        catch (ex: UnknownHostException){
            Resource.NoInternetConnection()
        }
        catch (ex:Exception){
            Resource.Error("Exception is $ex")
        }
    }
}