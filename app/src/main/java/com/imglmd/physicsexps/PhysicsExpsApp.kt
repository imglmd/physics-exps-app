package com.imglmd.physicsexps

import android.app.Application
import com.imglmd.physicsexps.di.mainModule
import org.koin.core.context.startKoin

class PhysicsExpsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(mainModule)
        }
    }
}