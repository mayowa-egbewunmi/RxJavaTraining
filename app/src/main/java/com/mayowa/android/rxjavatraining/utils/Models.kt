package com.mayowa.android.rxjavatraining.utils

data class ProxyUser(
    val email: String,
    val fullname: String
)

sealed class Result {

    data class SuccessResult(val data: ProxyUser) : Result()

    data class ErrorResult(val throwable: Throwable) : Result()
}
