package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.BuildConfig
import com.imglmd.physicsexps.data.TokenStorage
import com.imglmd.physicsexps.data.remote.ApiService
import com.imglmd.physicsexps.data.remote.AuthInterceptor
import com.imglmd.physicsexps.data.remote.RemoteConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val remoteModule = module {
    single {
        RemoteConfig(
            baseUrl = BuildConfig.BACKEND_BASE_URL,
        )
    }

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }

    single { TokenStorage(get()) }

    single { AuthInterceptor(get()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single<ApiService> {
        val networkJson: Json = get()
        val remoteConfig: RemoteConfig = get()
        Retrofit.Builder()
            .baseUrl(remoteConfig.baseUrl)
            .client(get<OkHttpClient>())
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}