package com.example.cleanarchitectureexample.data.local

import com.example.cleanarchitectureexample.data.remote.model.UsersResponse
import com.example.cleanarchitectureexample.network.Resource
import com.example.cleanarchitectureexample.presentation.UsersData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: UserDao) {

    fun getUsersFromLocalStorage() : Flow<Resource<List<UserEntity>>>{
        return flow {
            try {
                val getAllUsersListFromDB = dao.getAllUsers()
                emit(Resource.Success(data = getAllUsersListFromDB))
            }catch (ex: Exception){
                emit(Resource.Error(errorMessage = "Exception in db = $ex"))
            }
        }
    }

    suspend fun insertAllUsersInDB(usersList : List<UsersResponse>){

        val convertedUserList = usersList.map {
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

        dao.insertAllUsers(userEntities = convertedUserList)
    }
}