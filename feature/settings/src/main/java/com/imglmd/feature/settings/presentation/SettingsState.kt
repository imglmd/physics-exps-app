package com.imglmd.feature.settings.presentation

import com.imglmd.feature.settings.domain.model.AppLanguage
import com.imglmd.feature.settings.domain.model.AppSettings
import com.imglmd.feature.settings.domain.model.AppTheme

data class SettingsState(
    val settings: AppSettings = AppSettings(),
    val isLoading: Boolean = true,
)

sealed interface SettingsIntent  {
    data class ThemeChanged(val theme: AppTheme): SettingsIntent
    data class AmoledThemeChanged(val enabled: Boolean): SettingsIntent
    data class DynamicColorsChanged(val enabled: Boolean): SettingsIntent
    data class HapticFeedbackChanged(val enabled: Boolean): SettingsIntent

    data class OfflineModeChanged(val enabled: Boolean): SettingsIntent
    data class AdvancedModeChanged(val enabled: Boolean): SettingsIntent
    data class MaxHistoryChanged(val value: Int?): SettingsIntent

    data class ChangeLanguage(val lang: AppLanguage): SettingsIntent

}