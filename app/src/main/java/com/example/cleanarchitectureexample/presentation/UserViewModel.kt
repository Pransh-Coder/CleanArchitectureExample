package com.example.cleanarchitectureexample.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureexample.domain.GetUsersDataUseCase
import com.example.cleanarchitectureexample.network.NetworkResponse
import com.example.cleanarchitectureexample.presentation.uiState.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val getUsersDataUseCase: GetUsersDataUseCase):ViewModel() {

    private val _userDataState = MutableStateFlow(UserState(isLoading = false))
    val userDataState : StateFlow<UserState>
        get() = _userDataState

    private val _errors = Channel<String>()
    var errors = _errors.receiveAsFlow()

    fun getUsers(){
        viewModelScope.launch {
            Log.e(TAG, "getUsers: ", )
            getUsersDataUseCase.invoke().collectLatest { networkResponse ->
                when(networkResponse){
                    is NetworkResponse.Loading -> {
                        Log.e(TAG, "getUsers: Loading...", )
                        _userDataState.value = userDataState.value.copy(isLoading = true)
                    }
                    is NetworkResponse.Success -> {
                        delay(5000)
                        Log.e(TAG, "getUsers: success data = ${networkResponse.data}", )
                        _userDataState.value = userDataState.value.copy(isLoading = false, usersList = networkResponse.data)
                    }
                    is NetworkResponse.Error -> {
                        Log.e(TAG, "getUsers: errorMsg = ${networkResponse.errorMessage}", )
                        _userDataState.value = userDataState.value.copy(isLoading = false)

                        _errors.send(networkResponse.errorMessage)
                    }
                }
            }
        }
    }
    companion object{
        private const val TAG = "UserViewModel"
    }
}