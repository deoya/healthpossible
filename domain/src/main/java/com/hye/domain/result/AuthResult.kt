package com.hye.domain.result

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Failure(val exception: Throwable) : AuthResult<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): AuthResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onFailure(action: (Throwable) -> Unit): AuthResult<T> {
        if (this is Failure) action(exception)
        return this
    }
}