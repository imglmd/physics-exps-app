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

    suspend fun setHapticFeedback(enabled: Boolean) =
        repository.updateHapticFeedback(enabled)

    suspend fun setOfflineMode(enabled: Boolean) =
        repository.updateOfflineMode(enabled)
    suspend fun setAdvancedMode(enabled: Boolean) =
        repository.updateAdvancedMode(enabled)
    suspend fun setMaxHistory(value: Int?) =
        repository.updateMaxHistory(value)

    suspend fun setAppLanguage(lang: String) {
        repository.setAppLanguage(lang)
    }

    suspend fun getAppLang(): String =
        repository.getAppLanguage()

}