package com.imglmd.physicsexps.presentation.screens.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val resultRepository: InMemoryResultRepository
): ViewModel() {

    private val _state = MutableStateFlow<ResultContract.State>(ResultContract.State.Loading)
    val state = _state.asStateFlow()

    init {
        val result = resultRepository.get()
        _state.update {
            if (result != null) {
                ResultContract.State.Success(result)
            } else {
                ResultContract.State.Error("Результат не найден")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
    }
}