package com.imglmd.physicsexps.di

import com.imglmd.feature.history.historyModule
import com.imglmd.core.network.di.networkModule
import com.imglmd.core.experiments.experimentsModule
import com.imglmd.feature.compare.compareModule
import com.imglmd.feature.constants.di.constantsModule
import com.imglmd.feature.settings.di.settingsModule

val appModules = listOf(
    coreModule,
    networkModule,

    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    experimentsModule,
    navigationModule,
    remoteModule,

    constantsModule,
    settingsModule,
    historyModule,
    stringProvidersModule,
    compareModule
)