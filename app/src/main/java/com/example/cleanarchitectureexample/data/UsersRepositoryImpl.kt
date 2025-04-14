package com.example.cleanarchitectureexample.data

import com.example.cleanarchitectureexample.data.local.UserEntity
import com.example.cleanarchitectureexample.data.local.UserDao
import com.example.cleanarchitectureexample.data.remote.MappedUsersData
import com.example.cleanarchitectureexample.data.remote.model.UsersData
import com.example.cleanarchitectureexample.network.ApiInterface
import com.example.cleanarchitectureexample.network.NetworkResponse
import com.example.cleanarchitectureexample.network.invokeWithStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val userDao: UserDao,
) : UserRepoInterface {

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

                        saveUserDetailsInLocalDB(userDao = userDao, response = response)
                    },
                    onError = {
                        emit(NetworkResponse.Error(response.errorBody().toString()))
                    }
                )
            } catch (e: UnknownHostException) {
                emit(NetworkResponse.Error("No internet connection, Trying to fetch data from local DB $e"))

                //this is added here to show the loading of data from the local storage if there is no internet connection
                emit(NetworkResponse.Loading())     // Emitting loading again in case of failure
                val localUsersData = getUsersFromLocalDB(userDao = userDao)
                if (localUsersData.isNotEmpty()){
                    emit(NetworkResponse.Success(data = localUsersData))
                }
                else{
                    emit(NetworkResponse.Error(errorMessage = "No internet connection & no data in local db"))
                }
            } catch (exception: Exception) {
                emit(NetworkResponse.Error(errorMessage = exception.message ?: "Something went wrong!"))
            }
        }
    }


    private suspend fun saveUserDetailsInLocalDB(userDao: UserDao,response: Response<List<UsersData>>) {
        response.body()?.let {
            val usersList = it.map {
                UserEntity(
                    id = it.id,
                    name = it.name,
                    username = it.username,
                    email = it.email,
                    phone = it.phone,
                    address = "${it.address.suite} ${it.address.street}, ${it.address.city}, ${it.address.zipcode}",
                    company = "${it.company.name} | ${it.company.catchPhrase}",
                    website = it.website
                )
            }

            userDao.deleteAllUsers(usersList)

            userDao.insertAllUsers(usersList)
        }
    }

    private suspend fun getUsersFromLocalDB(userDao: UserDao) : List<MappedUsersData>{
        return userDao.getAllUsers().map {
            MappedUsersData(
                id = it.id,
                name = it.name,
                email = it.email,
                phone = it.phone,
                address = it.address,
                company = it.company,
            )
        }
    }

    companion object{
        private const val TAG = "UsersRepositoryImpl"
    }
}