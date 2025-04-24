package com.example.cleanarchitectureexample.network

sealed class Resource<T>(open val data:T? = null, open val errorMessage:String? = null) {

    //The main advantage of using data class in Kotlin is that it automatically provides these utility
    // methods (toString(), equals(), hashCode(), and copy()) for the class.

    //toString() -- allows you to easily print an instance of Success or Error and get a useful string representation.

    //equals() and hashCode() are automatically generated to compare objects based on their values, rather than references.

    //copy() provides an easy way to create a new instance of the class with the same values, but with the ability to change some properties.
    data class Success<T>(override val data: T) : Resource<T>(data)

    data class Error<T>(override val errorMessage: String) : Resource<T>(errorMessage = errorMessage)

    class NoInternetConnection<T>() : Resource<T>()
}