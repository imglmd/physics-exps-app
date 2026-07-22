package com.imglmd.physicsexps.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val APP_SCOPE = named("appScope")

val coreModule = module {
    single<CoroutineScope>(APP_SCOPE) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}