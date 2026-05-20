package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.data.remote.ApiService
import com.imglmd.physicsexps.data.remote.MediaRepositoryImpl
import com.imglmd.physicsexps.domain.repository.MediaRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val remoteModule = module {
    single<ApiService> {
        val networkJson = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}