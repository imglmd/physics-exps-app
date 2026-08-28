package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.usecase.auth.EnsureAuthorizedUseCase
import com.imglmd.physicsexps.domain.usecase.auth.PingUseCase
import com.imglmd.physicsexps.domain.usecase.auth.RegisterUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { PingUseCase(get()) }

    factory { RegisterUseCase(get()) }
    factory { EnsureAuthorizedUseCase(get(),get(), get()) }
}
