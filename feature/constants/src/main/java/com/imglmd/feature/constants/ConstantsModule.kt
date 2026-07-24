package com.imglmd.feature.constants

import com.imglmd.feature.constants.domain.usecase.GetAllCategoriesUseCase
import com.imglmd.feature.constants.presentation.ConstantsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val constantsModule = module {
    factory { GetAllCategoriesUseCase() }
    viewModel { ConstantsViewModel(get()) }
}