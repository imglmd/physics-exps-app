package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.BuildConfig
import com.imglmd.physicsexps.core.OfflineModeProviderImpl
import com.imglmd.physicsexps.core.OnlineStateManager
import com.imglmd.core.network.OfflineModeProvider
import com.imglmd.feature.experiment.data.remote.ApiService
import com.imglmd.feature.experiment.data.remote.AuthInterceptor
import com.imglmd.feature.experiment.data.remote.RemoteConfig
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

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

    single { AuthInterceptor(get()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }

    single<OfflineModeProvider> {
        OfflineModeProviderImpl(get())
    }

    single {
        OnlineStateManager(
            networkMonitor = get(),
            offlineModeProvider = get(),
            pingUseCase = get()
        )
    }
}