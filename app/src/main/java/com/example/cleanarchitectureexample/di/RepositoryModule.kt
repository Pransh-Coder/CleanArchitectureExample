package com.example.cleanarchitectureexample.di

import com.example.cleanarchitectureexample.data.local.UserDao
import com.example.cleanarchitectureexample.data.UserRepoInterface
import com.example.cleanarchitectureexample.data.UsersRepositoryImpl
import com.example.cleanarchitectureexample.network.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesRepository(apiInterface: ApiInterface, userDao: UserDao): UserRepoInterface {
        return UsersRepositoryImpl(apiInterface = apiInterface,userDao = userDao)
    }
}