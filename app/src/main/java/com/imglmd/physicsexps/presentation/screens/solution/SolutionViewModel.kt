package com.imglmd.physicsexps.presentation.screens.solution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imglmd.physicsexps.data.InMemoryResultRepository
import com.imglmd.physicsexps.domain.ExperimentRegistry
import com.imglmd.physicsexps.domain.model.SolutionStep
import com.imglmd.physicsexps.presentation.screens.history.HistoryContract
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SolutionViewModel(
    private val registry: ExperimentRegistry,
    private val repository: InMemoryResultRepository
): ViewModel() {

    private val _solutionSteps = MutableStateFlow<List<SolutionStep>>(emptyList())
    val solutionSteps = _solutionSteps.asStateFlow()

    private val _goBackFlow = MutableSharedFlow<Unit>(replay = 0)
    val goBackFlow = _goBackFlow.asSharedFlow()

    init {
        load()
    }

    private fun load(){
        viewModelScope.launch {
            val bundle = repository.get() ?: run {
                _goBackFlow.emit(Unit)
                return@launch
            }

            val experiment = registry.getById(bundle.result.experimentId)
            _solutionSteps.update { experiment.getSolutionSteps(bundle.inputs) }
        }
    }
}