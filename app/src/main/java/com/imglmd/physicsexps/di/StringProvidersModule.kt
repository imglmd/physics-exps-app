package com.imglmd.physicsexps.di

import com.imglmd.feature.history.presentation.HistoryStringProvider
import com.imglmd.core.ui.utils.StringKeyProvider
import com.imglmd.physicsexps.presentation.core.ExperimentsStringProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

val STRING_PROVIDERS = named("stringProviders")

val stringProvidersModule = module {
    single<List<StringKeyProvider>>(STRING_PROVIDERS) {
        listOf(
            ExperimentsStringProvider(),
            HistoryStringProvider(),
        )
    }
}