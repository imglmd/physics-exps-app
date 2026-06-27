package com.imglmd.physicsexps.feature.constants.di

import com.imglmd.physicsexps.feature.constants.domain.usecase.GetAllCategoriesUseCase
import com.imglmd.physicsexps.feature.constants.presentation.ConstantsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val constantsModule = module {
    factory { GetAllCategoriesUseCase() }
    viewModel { ConstantsViewModel(get()) }
}