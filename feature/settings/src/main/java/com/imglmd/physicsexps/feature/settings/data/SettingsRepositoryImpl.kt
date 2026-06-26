package com.imglmd.physicsexps.feature.settings.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import com.imglmd.physicsexps.feature.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val dataSource: SettingsDataSource
): SettingsRepository{
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

    override suspend fun updateOfflineMode(enabled: Boolean) {
        dataSource.updateOfflineMode(enabled)
    }

    override suspend fun updateAdvancedMode(enabled: Boolean){
        dataSource.updateAdvancedMode(enabled)
    }

    override suspend fun updateMaxHistory(value: Int?) {
        dataSource.updateMaxHistory(value)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun getAppLanguage(): String {
        return dataSource.getAppLanguage()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun setAppLanguage(lang: String) {
        dataSource.setAppLanguage(lang)
    }
}