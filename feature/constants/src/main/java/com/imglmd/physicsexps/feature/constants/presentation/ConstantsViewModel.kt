package com.imglmd.physicsexps.feature.constants.presentation

import androidx.lifecycle.ViewModel
import com.imglmd.physicsexps.feature.constants.domain.usecase.GetAllCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConstantsViewModel(
    getAllCategoriesUseCase: GetAllCategoriesUseCase
): ViewModel() {
    private val _state = MutableStateFlow(ConstantsContract.State(allCategories = getAllCategoriesUseCase()))
    val state = _state.asStateFlow()

    fun onIntent(intent: ConstantsContract.Intent) {
        when(intent) {
            is ConstantsContract.Intent.ChangeSearchText -> {_state.update { it.copy(search = intent.text) }}
            is ConstantsContract.Intent.ChangeDigits -> updatePreferences { copy(digits = intent.value) }
            is ConstantsContract.Intent.ChangeCopyMode -> updatePreferences { copy(copyMode = intent.mode) }
        }
    }

    private fun updatePreferences(
        transform: ConstantsPreferences.() -> ConstantsPreferences
    ) {
        _state.update { it.copy(preferences = it.preferences.transform()) }
    }
}
