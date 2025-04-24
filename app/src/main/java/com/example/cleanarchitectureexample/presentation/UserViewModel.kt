package com.example.cleanarchitectureexample.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureexample.domain.GetUsersDataUseCase
import com.example.cleanarchitectureexample.domain.GetUsersLocalDataUseCase
import com.example.cleanarchitectureexample.network.Resource
import com.example.cleanarchitectureexample.presentation.uiState.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val getUsersDataUseCase: GetUsersDataUseCase,
    val localDataUseCase: GetUsersLocalDataUseCase,
) : ViewModel() {

    private val _userDataState = MutableStateFlow(UserState(isLoading = false))
    val userDataState : StateFlow<UserState>
        get() = _userDataState

    //shared flow as name suggests are meant for mainly used when we have multiple subscribers/collectors to a single flow

    //while channels on the other side are meant for just single subscriber/collector

    // channels have an integrated buffer i.e if there are no collectors & we send an event into the channel,
    // that actually ends up in the buffer & as soon as collector appear again that collector will then
    // receive the event that was previously saved in channels buffer
    private val _errors = Channel<String>()
    var errors = _errors.receiveAsFlow()

    fun getUsers(){
        viewModelScope.launch {
            // Replaced `collectLatest` with `collect` to ensure we capture **all intermediate events**
            // Using `collectLatest` was skipping early emissions like internet error message & was
            // just getting the latest/last event i.e loading
            _userDataState.update { 
                it.copy(isLoading = true)
            }
            localDataUseCase.invoke().collectLatest { networkResponse ->
                Log.e(TAG, "getUsers from DB: networkResponse = $networkResponse", )
                when(networkResponse){
                    is Resource.Success -> {
                        val userDataFromDb = networkResponse.data
                        if (userDataFromDb.isNotEmpty()){
                            Log.e(TAG, "getUsers from DB: getting data from db.....")
                            _userDataState.value = userDataState.value.copy(isLoading = false, usersList = networkResponse.data)
                        }
                        else{
                            fetchUsersDataFromNetwork()
                        }
                    }
                    is Resource.NoInternetConnection -> {
                        _userDataState.update { 
                            it.copy(isLoading = false)
                        }
                        _errors.send("Please check your internet connection!!")
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "getUsers from DB: errorMsg = ${networkResponse.errorMessage}", )
                        _userDataState.value = userDataState.value.copy(isLoading = false)

                        _errors.send(networkResponse.errorMessage)
                    }
                }
            }
        }
    }

    private fun fetchUsersDataFromNetwork() {
        viewModelScope.launch { 
            val usersDataNetworkResponse = getUsersDataUseCase.invoke()
            when(usersDataNetworkResponse){
                is Resource.Success -> {
                    Log.e(TAG, "fetchUsersDataFromNetwork: data = ${usersDataNetworkResponse.data}")
                    _userDataState.value = userDataState.value.copy(
                        isLoading = false,
                        usersList = usersDataNetworkResponse.data
                    )
                }
                is Resource.NoInternetConnection -> {
                    _userDataState.update { 
                        it.copy(isLoading = false)
                    }
                    _errors.send("No Internet!")
                }
                is Resource.Error -> {
                    Log.e(TAG, "fetchUsersDataFromNetwork: error = ${usersDataNetworkResponse.errorMessage}", )
                    _userDataState.value = userDataState.value.copy(isLoading = false)
                    _errors.send(usersDataNetworkResponse.errorMessage)
                }
            }
        }
    }

    /*fun getUserById(id: Int){
        viewModelScope.launch {
            getUsersDataUseCase.invoke(id).collectLatest {
                when(it){
                    is Resource.Success ->{
                        Log.e(TAG, "getUserById: data = ${it.data}")
                    }
                    is Resource.Error ->{
                        Log.e(TAG, "getUserById: error = ${it.errorMessage}")
                    }
                    else -> Unit
                }
            }
        }
    }*/

    companion object{
        private const val TAG = "UserViewModel"
    }
}