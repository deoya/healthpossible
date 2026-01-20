package com.hye.domain.result

sealed class MissionResult <out T>{

    data object NoConstructor : MissionResult<Nothing>()

    data object Loading : MissionResult<Nothing>()

    data class Success<T>(val resultData: T) : MissionResult<T>()

    data class NetworkError(val exception: Throwable) : MissionResult<Nothing>()

    data class FirebaseError(val exception: Throwable) : MissionResult<Nothing>()

    data class RoomDBError(val exception: Throwable) : MissionResult<Nothing>()

}