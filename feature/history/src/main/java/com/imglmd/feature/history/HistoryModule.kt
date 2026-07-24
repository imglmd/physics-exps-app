package com.imglmd.feature.history

import com.imglmd.feature.history.data.InMemoryResultRepository
import com.imglmd.feature.history.data.repository.ExperimentRunsRepositoryImpl
import com.imglmd.feature.history.data.repository.ResultsRepositoryImpl
import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository
import com.imglmd.feature.history.domain.repository.ResultsRepository
import com.imglmd.feature.history.domain.usecase.DeleteAllRunsUseCase
import com.imglmd.feature.history.domain.usecase.DeleteRunUseCase
import com.imglmd.feature.history.domain.usecase.GetAllRunsUseCase
import com.imglmd.feature.history.domain.usecase.GetFilteredRunsUseCase
import com.imglmd.feature.history.domain.usecase.GetLastRunsUseCase
import com.imglmd.feature.history.domain.usecase.GetResultUseCase
import com.imglmd.feature.history.domain.usecase.GetRunUseCase
import com.imglmd.feature.history.domain.usecase.SaveRunUseCase
import com.imglmd.feature.history.presentation.HistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val historyModule = module {
    single { InMemoryResultRepository() }

    single<ExperimentRunsRepository> { ExperimentRunsRepositoryImpl(get()) }
    single<ResultsRepository> { ResultsRepositoryImpl(get()) }

    factory { SaveRunUseCase(get(), get(), get()) }
    factory { GetRunUseCase(get()) }
    factory { GetResultUseCase(get()) }
    factory { GetAllRunsUseCase(get()) }
    factory { GetLastRunsUseCase(get()) }
    factory { GetFilteredRunsUseCase(get(), get()) }
    factory { DeleteRunUseCase(get()) }
    factory { DeleteAllRunsUseCase(get()) }

    viewModel { params ->
        HistoryViewModel(
            preselectedIds = params.get(),
            getExperiment = get(),
            getResultUseCase = get(),
            getRunUseCase = get(),
            resultRepository = get(),
            deleteAllRunsUseCase = get(),
            getFilteredRunsUseCase = get(),
            getAllExperimentsUseCase = get()
        )
    }
}