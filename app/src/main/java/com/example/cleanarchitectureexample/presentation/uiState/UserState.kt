package com.example.cleanarchitectureexample.presentation.uiState

import com.example.cleanarchitectureexample.data.remote.MappedUsersData

data class UserState(
    val isLoading: Boolean = false,
    val usersList: List<MappedUsersData>? = null
)
