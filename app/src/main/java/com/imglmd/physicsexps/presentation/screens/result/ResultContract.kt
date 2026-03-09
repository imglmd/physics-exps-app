package com.imglmd.physicsexps.presentation.screens.result

import com.imglmd.physicsexps.domain.model.ExperimentResult

sealed interface ResultState{
    object Loading: ResultState
    data class Success(val result: ExperimentResult): ResultState
    data class Error(val message: String): ResultState
}