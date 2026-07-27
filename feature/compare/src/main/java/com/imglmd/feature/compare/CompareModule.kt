package com.imglmd.feature.compare

import com.imglmd.feature.compare.presentation.CompareViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val compareModule = module {
    viewModel { params ->
        CompareViewModel(
            params.get(),
            get(),
            get(),
            get()
        )
    }
}