package com.imglmd.core.auth


import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    single { TokenStorage(get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }
    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }
}