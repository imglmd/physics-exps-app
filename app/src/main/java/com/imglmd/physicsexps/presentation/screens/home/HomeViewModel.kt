package com.imglmd.physicsexps.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.imglmd.physicsexps.domain.usecase.GetAllExperimentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val getAllExperimentsUseCase: GetAllExperimentsUseCase
): ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadExperiments()
    }


    fun onSearchTextChange(text: String) {
        _state.update { current ->
            val filtered = getAllExperimentsUseCase()
                .filter {
                    it.name.contains(text, ignoreCase = true)
                }

            current.copy(
                searchText = text,
                experiments = filtered
            )
        }
    }
    private fun loadExperiments(){
        _state.update {
            it.copy(experiments = getAllExperimentsUseCase())
        }
    }
}