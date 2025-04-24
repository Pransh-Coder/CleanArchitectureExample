package com.example.cleanarchitectureexample.di

import com.example.cleanarchitectureexample.data.local.UserDao
import com.example.cleanarchitectureexample.data.UserRepoInterface
import com.example.cleanarchitectureexample.data.UsersRepositoryImpl
import com.example.cleanarchitectureexample.data.local.LocalDataSource
import com.example.cleanarchitectureexample.data.remote.RemoteDataSource
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
    fun providesRemoteDataSource(apiInterface: ApiInterface) : RemoteDataSource{
        return RemoteDataSource(apiInterface = apiInterface)
    }

    @Provides
    @Singleton
    fun providesLocalDataSource(dao: UserDao): LocalDataSource{
        return LocalDataSource(dao = dao)
    }

    @Provides
    @Singleton
    fun providesRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): UserRepoInterface {
        return UsersRepositoryImpl(remoteDataSource = remoteDataSource, localDataSource = localDataSource)
    }
}