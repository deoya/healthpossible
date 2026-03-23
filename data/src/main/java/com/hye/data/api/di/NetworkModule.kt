package com.hye.data.api.di

import com.hye.data.api.service.DiseaseApiService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTikXml(): TikXml {
        return TikXml.Builder()
            .exceptionOnUnreadXml(false) // 정의하지 않은 XML 태그는 안전하게 무시
            .build()
    }

    @Provides
    @Singleton
    fun provideRetryInterceptor(): RetryInterceptor {
        return RetryInterceptor(maxRetry = 3)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        retryInterceptor: RetryInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private const val KDCA_BASE_URL = "https://api.kdca.go.kr/"

    @Provides
    @Singleton
    fun provideDiseaseRetrofit(
        okHttpClient: OkHttpClient,
        tikXml: TikXml
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KDCA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(TikXmlConverterFactory.create(tikXml)) // TikXml 장착
            .build()
    }

    @Provides
    @Singleton
    fun provideDiseaseApiService(retrofit: Retrofit): DiseaseApiService {
        return retrofit.create(DiseaseApiService::class.java)
    }
}