package com.imglmd.physicsexps.feature.settings.data

import android.content.Context
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsDataSource(private val context: Context){
    private val Context.dataStore by preferencesDataStore(name = "settings")

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val AMOLED_THEME = booleanPreferencesKey("amoled_theme")
    }

    val settings: Flow<AppSettings> = context.dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }
        .map { prefs ->
            AppSettings(
                theme = prefs[Keys.THEME]
                    ?.let { runCatching { AppTheme.valueOf(it) }.getOrDefault(AppTheme.SYSTEM) }
                    ?: AppTheme.SYSTEM,
                amoledTheme = prefs[Keys.AMOLED_THEME] ?: false
            )
        }

    suspend fun updateTheme(theme: AppTheme) {
        context.dataStore.edit { it[Keys.THEME] = theme.name }
    }
    suspend fun updateAmoledTheme(enabled: Boolean) {
        context.dataStore.edit {
            it[Keys.AMOLED_THEME] = enabled
        }
    }
}