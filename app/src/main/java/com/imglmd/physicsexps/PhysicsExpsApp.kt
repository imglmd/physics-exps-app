package com.imglmd.physicsexps

import android.app.Application
import com.imglmd.physicsexps.di.experimentsModule
import com.imglmd.physicsexps.di.mainModule
import com.imglmd.physicsexps.di.navigationModule
import org.koin.core.context.startKoin

class PhysicsExpsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(mainModule, experimentsModule, navigationModule)
        }
    }
}