package com.imglmd.physicsexps

import android.app.Application
import com.imglmd.physicsexps.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PhysicsExpsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(appModules)
            androidContext(this@PhysicsExpsApp)
        }
    }
}