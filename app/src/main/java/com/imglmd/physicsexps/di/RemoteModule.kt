package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.BuildConfig
import com.imglmd.physicsexps.data.remote.ApiService
import com.imglmd.physicsexps.data.remote.RemoteConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException

val remoteModule = module {
    single {
        RemoteConfig(
            baseUrl = BuildConfig.BACKEND_BASE_URL,
            apiKey = BuildConfig.BACKEND_API_KEY
        )
    }

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }

    single {
        val remoteConfig: RemoteConfig = get()
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val apiKey = remoteConfig.apiKey.trim()
                if (apiKey.isBlank()) {
                    throw IOException(
                        "BACKEND_API_KEY is not configured"
                    )
                }

                val request = chain.request().newBuilder()
                    .header("X-API-Key", apiKey)
                    .build()
                chain.proceed(request)
            }
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
