package com.imglmd.physicsexps.presentation.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val resultRepository: InMemoryResultRepository
): ViewModel() {

    private val _state = MutableStateFlow<ResultState>(ResultState.Loading)
    val state = _state.asStateFlow()

    private val _actionFlow = MutableSharedFlow<ResultAction>()
    val actionFlow = _actionFlow.asSharedFlow()

    init {
        val result = resultRepository.get()
        _state.update {
            if (result != null) {
                ResultState.Success(result)
            } else {
                ResultState.Error("Результат не найден")
            }
        }
    }

    fun onIntent(intent: ResultIntent) {
        when (intent) {
            is ResultIntent.Save -> saveResult()
            is ResultIntent.Delete -> deleteResult()
        }
    }

    private fun saveResult() {
        //TODO: save
        navigateHome()
    }

    private fun deleteResult() {
        resultRepository.clear()
        navigateHome()
    }

    private fun navigateHome() = viewModelScope.launch {
        _actionFlow.emit(ResultAction.NavigateHome)
    }
}