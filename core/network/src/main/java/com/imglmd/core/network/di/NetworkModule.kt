package com.imglmd.core.network.di

import com.imglmd.core.network.NetworkMonitor
import com.imglmd.core.network.NetworkMonitorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single<NetworkMonitor> { NetworkMonitorImpl(androidContext()) }

}