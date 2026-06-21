package com.imglmd.physicsexps.feature.settings.data

import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val dataSource: SettingsDataSource
): SettingsRepository {
    override val settings = dataSource.settings

    override suspend fun updateTheme(theme: AppTheme) {
        dataSource.updateTheme(theme)
    }
    override suspend fun updateAmoledTheme(enabled: Boolean) {
        dataSource.updateAmoledTheme(enabled)
    }
    override suspend fun updateDynamicColors(enabled: Boolean) {
        dataSource.updateDynamicColors(enabled)
    }

    override suspend fun updateHapticFeedback(enabled: Boolean){
        dataSource.updateHapticFeedback(enabled)
    }

    override suspend fun updateAdvancedMode(enabled: Boolean){
        dataSource.updateAdvancedMode(enabled)
    }

    override suspend fun updateMaxHistory(value: Int?) {
        dataSource.updateMaxHistory(value)
    }
}