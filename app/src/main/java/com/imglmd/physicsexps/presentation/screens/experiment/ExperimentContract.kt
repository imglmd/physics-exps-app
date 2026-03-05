package com.imglmd.physicsexps.presentation.screens.experiment

import com.imglmd.physicsexps.domain.model.InputField

interface ExperimentContract {
    data class State (
        val fields: List<InputField>,
        val error: String? = null,
        val isLoading: Boolean = false,
        val isButtonActive: Boolean = true
    )
    sealed interface Intent {
        data object Start : Intent
        data class ChangeValue(val key: String, val newValue: String): Intent
    }
}