package com.example.cleanarchitectureexample.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.cleanarchitectureexample.network.ApiInterface
import com.example.cleanarchitectureexample.Constants
import com.example.cleanarchitectureexample.Constants.DATABASE_NAME
import com.example.cleanarchitectureexample.data.local.UserDao
import com.example.cleanarchitectureexample.data.local.UserDataBase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesBaseUrl(): String {
        return Constants.BASE_URL
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setStrictness(Strictness.LENIENT).create()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(@ApplicationContext context: Context): OkHttpClient {

        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context = context))
            .maxContentLength(250_000L)
            .alwaysReadResponseBody(true)
            .build()

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)

        return OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .connectTimeout(600000, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun providesUserDataBase(@ApplicationContext context: Context): UserDataBase {
        return Room.databaseBuilder(context, UserDataBase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun providesUserDao(userDataBase: UserDataBase): UserDao {
        return userDataBase.userDao()
    }
}