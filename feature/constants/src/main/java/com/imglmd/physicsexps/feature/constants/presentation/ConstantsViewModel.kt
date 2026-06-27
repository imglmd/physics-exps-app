package com.imglmd.physicsexps.feature.constants.presentation

import androidx.lifecycle.ViewModel
import com.imglmd.physicsexps.feature.constants.domain.usecase.GetAllCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConstantsViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
): ViewModel() {
    private val allCategories = getAllCategoriesUseCase()

    private val _state = MutableStateFlow(ConstantsContract.State())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(search = "",
            allCategories = allCategories
        )}
    }

    fun onIntent(intent: ConstantsContract.Intent) {
        when(intent) {
            is ConstantsContract.Intent.ChangeSearchText -> {_state.update { it.copy(search = intent.text) }}
        }
    }
}
