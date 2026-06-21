package com.imglmd.physicsexps.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.feature.settings.domain.usecase.GetSettingsUseCase
import com.imglmd.physicsexps.feature.settings.domain.usecase.UpdateSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettings: GetSettingsUseCase,
    private val updateSettings: UpdateSettingsUseCase
): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getSettings().collect { settings ->
                _state.update { it.copy(settings = settings, isLoading = false) }
            }
        }
    }

    fun onIntent(intent: SettingsIntent){
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.ThemeChanged -> updateSettings.setTheme(intent.theme)
                is SettingsIntent.AmoledThemeChanged -> updateSettings.setAmoledTheme(intent.enabled)
                is SettingsIntent.DynamicColorsChanged -> updateSettings.setDynamicColors(intent.enabled)
                is SettingsIntent.HapticFeedbackChanged -> updateSettings.setHapticFeedback(intent.enabled)

                is SettingsIntent.MaxHistoryChanged -> updateSettings.setMaxHistory(intent.value)
            }
        }
    }

}