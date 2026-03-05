package com.imglmd.physicsexps.presentation.screens.experiment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExperimentViewModel(
    private val id: Int
): ViewModel() {
    private val _state = MutableStateFlow(ExperimentContract.State(emptyList()))
    val state = _state.asStateFlow()

    fun onIntent(intent: ExperimentContract.Intent){
        when(intent){
            is ExperimentContract.Intent.ChangeValue -> changeValue(intent.key, intent.newValue)
            ExperimentContract.Intent.Start -> start()
        }
    }

    private fun start() {
        _state.update { it.copy(isLoading = true) }

    }

    private fun changeValue(key: String, newValue: String){

    }
}