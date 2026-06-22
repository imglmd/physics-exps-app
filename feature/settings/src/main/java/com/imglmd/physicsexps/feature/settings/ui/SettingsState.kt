package com.imglmd.physicsexps.feature.settings.ui

import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme

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
}