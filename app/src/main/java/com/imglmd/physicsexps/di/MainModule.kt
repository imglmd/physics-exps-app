package com.imglmd.physicsexps.di

import androidx.room.Room
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.data.database.ExpDb
import com.imglmd.physicsexps.data.repositoryImpl.ExperimentRunRepositoryImpl
import com.imglmd.physicsexps.domain.usecase.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.GetExperimentByIdUseCase
import com.imglmd.physicsexps.presentation.screens.experiment.ExperimentViewModel
import com.imglmd.physicsexps.presentation.screens.home.HomeViewModel
import com.imglmd.physicsexps.presentation.screens.result.ResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { InMemoryResultRepository() }

    factory { GetAllExperimentsUseCase(get()) }
    factory { GetExperimentByIdUseCase(get()) }
    factory { CalculateExperimentUseCase(get()) }
    viewModel {
        HomeViewModel(get())
    }
    viewModel { params ->
        ExperimentViewModel(params.get(), get(), get(), get())
    }
    viewModel {
        ResultViewModel(get())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            ExpDb::class.java,
            "exp_db"
        ).build()
    }
    single{get<ExpDb>().dao()}
    single { ExperimentRunRepositoryImpl(get())}
}