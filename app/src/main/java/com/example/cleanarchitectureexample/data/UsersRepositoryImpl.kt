package com.example.cleanarchitectureexample.data

import com.example.cleanarchitectureexample.data.local.LocalDataSource
import com.example.cleanarchitectureexample.data.local.UserDao
import com.example.cleanarchitectureexample.data.local.UserEntity
import com.example.cleanarchitectureexample.data.remote.RemoteDataSource
import com.example.cleanarchitectureexample.presentation.UsersData
import com.example.cleanarchitectureexample.data.remote.model.UsersResponse
import com.example.cleanarchitectureexample.network.ApiInterface
import com.example.cleanarchitectureexample.network.Resource
import com.example.cleanarchitectureexample.network.invokeWithStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

//A Repository is just a class that helps you manage and get data

//The RepositoryImpl is the actual class that does the real work & says "how those functions work"
class UsersRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : UserRepoInterface {

    override suspend fun getUsersFromNetwork(): Resource<List<UsersResponse>> {
        return remoteDataSource.getUserListFromNetwork()
    }

    override fun getUsersFromDataBase(): Flow<Resource<List<UserEntity>>> {
       return localDataSource.getUsersFromLocalStorage()
    }

/*
    override fun getUserDetailsById(id: Int): Flow<Resource<UsersData>> {
        return flow {
            try {
                val userEntityData = userDao.getUserDataById(id)

                val mappedData = UsersData(
                    id = userEntityData.id,
                    name = userEntityData.name,
                    email = userEntityData.email,
                    phone = userEntityData.phone,
                    address = userEntityData.address,
                    company = userEntityData.company
                )
                emit(Resource.Success(data = mappedData))
            }catch (ex: Exception){
                emit(Resource.Error(errorMessage = "Could not fetch the record from the table $ex"))
            }

        }
    }
*/

    companion object{
        private const val TAG = "UsersRepositoryImpl"
    }
}