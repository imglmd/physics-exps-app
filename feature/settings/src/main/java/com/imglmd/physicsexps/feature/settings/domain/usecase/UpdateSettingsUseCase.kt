package com.imglmd.physicsexps.feature.settings.domain.usecase

import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.domain.repository.SettingsRepository

class UpdateSettingsUseCase(private val repository: SettingsRepository) {

    suspend fun setTheme(theme: AppTheme) =
        repository.updateTheme(theme)

    suspend fun setAmoledTheme(enabled: Boolean) =
        repository.updateAmoledTheme(enabled)

    suspend fun setDynamicColors(enabled: Boolean) =
        repository.updateDynamicColors(enabled)

    suspend fun setMaxHistory(value: Int?) =
        repository.updateMaxHistory(value)
}