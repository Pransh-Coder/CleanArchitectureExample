package com.example.cleanarchitectureexample.data

import com.example.cleanarchitectureexample.data.remote.MappedUsersData
import com.example.cleanarchitectureexample.network.ApiInterface
import com.example.cleanarchitectureexample.network.NetworkResponse
import com.example.cleanarchitectureexample.network.invokeWithStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface):
    UserRepoInterface {

    override fun getUsers(): Flow<NetworkResponse<List<MappedUsersData>>> {
        return flow {
            emit(NetworkResponse.Loading())
            try {
                val response = apiInterface.getUsers()
                response.code().invokeWithStatus(
                    onSuccess = {
                        val mappedUsersData = response.body()?.map {
                            MappedUsersData(
                                id = it.id,
                                name = it.name,
                                email = it.email,
                                phone = it.phone,
                                address = "${it.address.suite} ${it.address.street}, ${it.address.city}, ${it.address.zipcode}",
                                company = "${it.company.name} | ${it.company.catchPhrase}"
                            )
                        } ?: emptyList()
                        emit(NetworkResponse.Success(data = mappedUsersData))
                    },
                    onError = {
                        emit(NetworkResponse.Error(response.errorBody().toString()))
                    }
                )
            } catch (e: UnknownHostException) {
                emit(NetworkResponse.Error("No internet connection"))
            } catch (exception: Exception) {
                emit(NetworkResponse.Error(errorMessage = exception.message?:"Something went wrong!"))
            }
        }
    }
}