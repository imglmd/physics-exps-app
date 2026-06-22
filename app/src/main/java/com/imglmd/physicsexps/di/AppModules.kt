package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.core.network.di.networkModule
import com.imglmd.physicsexps.feature.settings.di.settingsModule

val appModules = listOf(
    networkModule,

    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    experimentsModule,
    navigationModule,
    remoteModule,

    settingsModule
)