package com.imglmd.physicsexps.feature.settings.domain.repository

import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun updateTheme(theme: AppTheme)
    suspend fun updateAmoledTheme(enabled: Boolean)
    suspend fun updateDynamicColors(enabled: Boolean)
    suspend fun updateHapticFeedback(enabled: Boolean)

    suspend fun updateMaxHistory(value: Int?)
}