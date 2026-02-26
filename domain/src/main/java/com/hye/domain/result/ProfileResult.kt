package com.hye.domain.result

sealed class ProfileResult<out T> {
    data class Success<out T>(val data: T) : ProfileResult<T>()
    data class Failure(val exception: Throwable) : ProfileResult<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): ProfileResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onFailure(action: (Throwable) -> Unit): ProfileResult<T> {
        if (this is Failure) action(exception)
        return this
    }
}