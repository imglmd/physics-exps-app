package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.feature.settings.di.settingsModule

val appModules = listOf(
    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    experimentsModule,
    navigationModule,
    remoteModule,
    settingsModule
)