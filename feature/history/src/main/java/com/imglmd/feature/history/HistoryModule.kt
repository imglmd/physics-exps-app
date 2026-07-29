package com.imglmd.feature.history

import com.imglmd.feature.history.domain.usecase.GetFilteredRunsUseCase
import com.imglmd.feature.history.presentation.HistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val historyModule = module {
    factory { GetFilteredRunsUseCase(get(), get()) }

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