package com.example.cleanarchitectureexample.data

import com.example.cleanarchitectureexample.data.remote.MappedUsersData
import com.example.cleanarchitectureexample.network.NetworkResponse
import kotlinx.coroutines.flow.Flow

//An interface is like a contract - "telling what functions are available"
interface UserRepoInterface {

    fun getUsers() : Flow<NetworkResponse<List<MappedUsersData>>>

    fun getUserDetailsById(id: Int): Flow<NetworkResponse<MappedUsersData>>
}

/*

Why separate Interface and Implementation?

    1. Flexibility
       ** You can switch your data source without changing your whole app. **

       Suppose, earlier you were using Network Source to get the data, but tomorrow you don't want
       to use API anymore instead FirebaseDatabase so, No problem! just create a new UserRepositoryImpl
       that uses FirebaseDatabase instead of API. The rest of the app doesn't change — it still talks to the same interface.

       2. Easier Testing
       ** When writing unit tests, you don't need to call the real API's, you want to use the fake responses **

       3. Clean Architecture
       ** In a clean app, we keep layers(UI, logic, data) separate **

       By using interfaces, you keep your app well-organized and easy to manage.
 */