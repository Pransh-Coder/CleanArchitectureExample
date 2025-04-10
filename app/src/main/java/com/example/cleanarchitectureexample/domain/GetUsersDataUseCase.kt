package com.example.cleanarchitectureexample.domain

import com.example.cleanarchitectureexample.data.UserRepoInterface
import javax.inject.Inject

class GetUsersDataUseCase @Inject constructor(private val repository: UserRepoInterface) {

    operator fun invoke() = repository.getUsers()


    companion object{
        private const val TAG = "GetUsersDataUseCase"
    }
}