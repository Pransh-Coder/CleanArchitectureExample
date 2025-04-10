package com.example.cleanarchitectureexample.network

import com.example.cleanarchitectureexample.Constants

suspend fun Int.invokeWithStatus(onSuccess: (suspend () -> Unit)?, onError: (suspend () -> Unit)?) {

   when(this){
       Constants.CODE_200 -> onSuccess?.invoke()
       else -> onError?.invoke()
   }
}