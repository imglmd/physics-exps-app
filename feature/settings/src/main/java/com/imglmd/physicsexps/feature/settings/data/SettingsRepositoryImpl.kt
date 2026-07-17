package com.imglmd.physicsexps.feature.settings.data

import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import com.imglmd.physicsexps.feature.settings.domain.model.AppLanguage
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class SettingsRepositoryImpl(
    private val dataSource: SettingsDataSource
): SettingsRepository{
    //TODO сделать лучше
    private var cachedSettings: AppSettings? = null

    override val settings: Flow<AppSettings> = dataSource.settings
        .onEach { cachedSettings = it }

    override fun getCachedSettings(): AppSettings? = cachedSettings

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

    override suspend fun updateOfflineMode(enabled: Boolean) {
        dataSource.updateOfflineMode(enabled)
    }

    override suspend fun updateAdvancedMode(enabled: Boolean){
        dataSource.updateAdvancedMode(enabled)
    }

    override suspend fun updateMaxHistory(value: Int?) {
        dataSource.updateMaxHistory(value)
    }

    override suspend fun updateLanguage(language: AppLanguage) {
        dataSource.setLanguage(language)
    }
}