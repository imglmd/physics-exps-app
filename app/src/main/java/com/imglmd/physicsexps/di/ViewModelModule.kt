package com.imglmd.physicsexps.di

import com.imglmd.physicsexps.domain.validation.ExperimentValidator
import com.imglmd.physicsexps.presentation.screens.compare.CompareViewModel
import com.imglmd.physicsexps.presentation.screens.experiment.ExperimentViewModel
import com.imglmd.physicsexps.presentation.screens.history.HistoryViewModel
import com.imglmd.physicsexps.presentation.screens.home.HomeViewModel
import com.imglmd.physicsexps.presentation.screens.result.ResultViewModel
import com.imglmd.physicsexps.presentation.screens.solution.SolutionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    single { ExperimentValidator() }

    viewModel {
        HomeViewModel(
            application = androidApplication(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel { params ->

        val inputs: Map<String, String>? = params.getOrNull()
        val replaceRunId: Int? = params.getOrNull()

        ExperimentViewModel(
            id = params.get(),
            inputs = inputs,
            replaceRunId = replaceRunId,
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel { params ->

        val runId: Int? = params.getOrNull()

        ResultViewModel(
            runId,
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel { params ->

        HistoryViewModel(
            params.get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel {
        SolutionViewModel(get(), get())
    }

    viewModel { params ->
        CompareViewModel(
            params.get(),
            get(),
            get(),
            get()
        )
    }
}
