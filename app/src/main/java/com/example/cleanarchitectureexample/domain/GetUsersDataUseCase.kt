package com.example.cleanarchitectureexample.domain

import com.example.cleanarchitectureexample.data.UserRepoInterface
import com.example.cleanarchitectureexample.network.NetworkResponse
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetUsersDataUseCase @Inject constructor(private val repository: UserRepoInterface) {

    operator fun invoke() = repository.getUsers().transform {
        val sortedListByName = it.data?.sortedBy { it.name }
        when(it){
            is NetworkResponse.Loading -> {
                emit(NetworkResponse.Loading())
            }
            is NetworkResponse.Success -> {
                emit(NetworkResponse.Success(sortedListByName))
            }
            is NetworkResponse.Error ->{
                emit(NetworkResponse.Error(it.errorMessage))
            }
        }
    }

    operator fun invoke(id: Int) = repository.getUserDetailsById(id)


    companion object{
        private const val TAG = "GetUsersDataUseCase"
    }
}