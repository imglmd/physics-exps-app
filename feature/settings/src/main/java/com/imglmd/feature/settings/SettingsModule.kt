package com.imglmd.feature.settings

import com.imglmd.feature.settings.data.SettingsDataSource
import com.imglmd.feature.settings.data.SettingsRepositoryImpl
import com.imglmd.feature.settings.domain.repository.SettingsRepository
import com.imglmd.feature.settings.domain.usecase.GetSettingsUseCase
import com.imglmd.feature.settings.domain.usecase.UpdateSettingsUseCase
import com.imglmd.feature.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single { SettingsDataSource(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    factory { UpdateSettingsUseCase(get()) }
    factory { GetSettingsUseCase(get()) }
    viewModel { SettingsViewModel(get(), get()) }
}