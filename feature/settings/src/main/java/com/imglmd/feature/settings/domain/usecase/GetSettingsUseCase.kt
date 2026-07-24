package com.imglmd.feature.settings.domain.usecase

import com.imglmd.feature.settings.domain.model.AppSettings
import com.imglmd.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<AppSettings> = repository.settings
    fun getCached() = repository.getCachedSettings()
}