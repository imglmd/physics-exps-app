package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.core.ui.utils.StringKeyProvider
import com.imglmd.physicsexps.presentation.core.ExperimentsStringProvider
import org.koin.dsl.module

val stringProvidersModule = module {
    single<List<StringKeyProvider>> {
        listOf(
            ExperimentsStringProvider(),
            HistoryStringProvider(),
        )
    }
}