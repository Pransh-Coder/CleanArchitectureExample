package com.example.cleanarchitectureexample.presentation.uiState

import com.example.cleanarchitectureexample.presentation.UsersData

data class UserState(
    val isLoading: Boolean = false,
    val usersList: List<UsersData>? = null
)
