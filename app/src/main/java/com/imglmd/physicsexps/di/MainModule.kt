package com.imglmd.physicsexps.di

import androidx.room.Room
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.data.database.ExpDb
import com.imglmd.physicsexps.data.repository.ExperimentRunsRepositoryImpl
import com.imglmd.physicsexps.data.repository.ResultsRepositoryImpl
import com.imglmd.physicsexps.data.repositoryImpl.CommentRepositoryImpl
import com.imglmd.physicsexps.domain.repository.CommentRepository
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.repository.ResultsRepository
import com.imglmd.physicsexps.domain.usecase.experiment.CalculateExperimentUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetAllExperimentsUseCase
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentByIdUseCase
import com.imglmd.physicsexps.domain.usecase.run.DeleteRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetAllRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetLastRunsUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetResultUseCase
import com.imglmd.physicsexps.domain.usecase.run.GetRunUseCase
import com.imglmd.physicsexps.domain.usecase.run.SaveRunUseCase
import com.imglmd.physicsexps.domain.validation.ExperimentValidator
import com.imglmd.physicsexps.presentation.screens.experiment.ExperimentViewModel
import com.imglmd.physicsexps.presentation.screens.history.HistoryViewModel
import com.imglmd.physicsexps.presentation.screens.home.HomeViewModel
import com.imglmd.physicsexps.presentation.screens.result.ResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { InMemoryResultRepository() }
    single<ResultsRepository> { ResultsRepositoryImpl(get()) }
    single { ExperimentValidator() }

    factory { SaveRunUseCase(get(), get()) }
    factory { DeleteRunUseCase(get(), get()) }
    factory { GetAllRunsUseCase(get()) }
    factory { GetRunUseCase(get()) }
    factory { GetResultUseCase(get()) }
    factory { GetAllExperimentsUseCase(get()) }
    factory { GetExperimentByIdUseCase(get()) }
    factory { CalculateExperimentUseCase(get(),get()) }
    factory { GetLastRunsUseCase(get()) }
    viewModel {
        HomeViewModel(get(), get(), get(), get(), get(), get())
    }
    viewModel { params ->
        val inputs: Map<String, String>? = params.getOrNull()
        ExperimentViewModel(params.get(), inputs, get(), get(), get())
    }
    viewModel { params ->
        val runId: Int? = params.getOrNull()
        ResultViewModel(runId, get(), get(), get(), get())
    }
    viewModel {
        HistoryViewModel(get(), get(), get(), get(), get())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            ExpDb::class.java,
            "exp_db"
        ).build()
    }
    single { get<ExpDb>().resDao() }
    single{get<ExpDb>().dao()}
    single{get<ExpDb>().comDao()}
    single<ExperimentRunsRepository> { ExperimentRunsRepositoryImpl(get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
}