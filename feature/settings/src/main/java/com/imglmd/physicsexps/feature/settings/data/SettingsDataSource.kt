package com.imglmd.physicsexps.feature.settings.data

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.imglmd.physicsexps.feature.settings.domain.model.AppSettings
import com.imglmd.physicsexps.feature.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.Locale

class SettingsDataSource(private val context: Context){
    private val Context.dataStore by preferencesDataStore(name = "settings")

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun setAppLanguage(languageTag: String) {
        val localeManager = context.getSystemService(LocaleManager::class.java)
        val localeList = LocaleList(Locale.forLanguageTag(languageTag))

        localeManager.applicationLocales = localeList
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getAppLanguage(): String {
        val localeManager = context.getSystemService(LocaleManager::class.java)
        val currentLocales = localeManager.applicationLocales

        return if (!currentLocales.isEmpty) {
            currentLocales.get(0).toLanguageTag()
        } else {
            Locale.getDefault().toLanguageTag()
        }
    }

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val AMOLED_THEME = booleanPreferencesKey("amoled_theme")
        val DYNAMIC_COLORS = booleanPreferencesKey("dynamic_colors")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")

        val OFFLINE_MODE = booleanPreferencesKey("offline_mode")
        val ADVANCED_MODE = booleanPreferencesKey("advanced_mode")
        val MAX_HISTORY = intPreferencesKey("max_history")
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
                amoledTheme = prefs[Keys.AMOLED_THEME] ?: false,
                dynamicColors = prefs[Keys.DYNAMIC_COLORS] ?: false,
                hapticFeedback = prefs[Keys.HAPTIC_FEEDBACK] ?: false,
                offlineMode = prefs[Keys.OFFLINE_MODE]?: false,
                advancedMode = prefs[Keys.ADVANCED_MODE] ?: false,
                maxHistoryEntries = prefs[Keys.MAX_HISTORY]
            )
        }

    suspend fun updateTheme(theme: AppTheme) =
        context.dataStore.edit { it[Keys.THEME] = theme.name }

    suspend fun updateAmoledTheme(enabled: Boolean) =
        context.dataStore.edit { it[Keys.AMOLED_THEME] = enabled }

    suspend fun updateDynamicColors(enabled: Boolean) =
        context.dataStore.edit { it[Keys.DYNAMIC_COLORS] = enabled }

    suspend fun updateHapticFeedback(enabled: Boolean) =
        context.dataStore.edit { it[Keys.HAPTIC_FEEDBACK] = enabled }

    suspend fun updateOfflineMode(enabled: Boolean) =
        context.dataStore.edit { it[Keys.OFFLINE_MODE] = enabled }
    suspend fun updateAdvancedMode(enabled: Boolean) =
        context.dataStore.edit { it[Keys.ADVANCED_MODE] = enabled }

    suspend fun updateMaxHistory(value: Int?) {
        context.dataStore.edit {
            if (value == null)  it.remove(Keys.MAX_HISTORY)
            else  it[Keys.MAX_HISTORY] = value
        }
    }
}