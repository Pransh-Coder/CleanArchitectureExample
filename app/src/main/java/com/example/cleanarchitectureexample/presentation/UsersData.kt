package com.example.cleanarchitectureexample.presentation

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UsersData(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val company: String
): Parcelable