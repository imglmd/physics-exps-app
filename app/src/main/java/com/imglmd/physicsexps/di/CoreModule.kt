package com.imglmd.physicsexps.di

import com.imglmd.feature.experiment.data.remote.RemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val coreModule = module {
    single<CoroutineScope>(named("appScope")) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }


    single {
        val networkJson: Json = get()
        val remoteConfig: RemoteConfig = get()
        Retrofit.Builder()
            .baseUrl(remoteConfig.baseUrl)
            .client(get<OkHttpClient>())
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}