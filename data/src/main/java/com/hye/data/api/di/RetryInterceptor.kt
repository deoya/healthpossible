package com.hye.data.api.di

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetry: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var attempt = 0
        var exception: IOException? = null

        while (attempt < maxRetry) {
            try {
                response?.close()
                response = chain.proceed(request)

                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                exception = e
            }
            attempt++
        }

        return response ?: throw exception ?: IOException("네트워크 통신 $maxRetry 회 재시도 실패")
    }
}