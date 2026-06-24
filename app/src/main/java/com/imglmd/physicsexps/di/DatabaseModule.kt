package com.imglmd.physicsexps.di

import androidx.room.Room
import com.imglmd.physicsexps.data.database.ExpDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            ExpDb::class.java,
            "exp_db"
        ).fallbackToDestructiveMigration(true).build()
    }

    single { get<ExpDb>().dao() }

    single { get<ExpDb>().resDao() }

    single { get<ExpDb>().comDao() }
}