package com.imglmd.physicsexps.core.network.di

import com.imglmd.physicsexps.core.network.NetworkMonitor
import com.imglmd.physicsexps.core.network.NetworkMonitorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single<NetworkMonitor> { NetworkMonitorImpl(androidContext()) }

}