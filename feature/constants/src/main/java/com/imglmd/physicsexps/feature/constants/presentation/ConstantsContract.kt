package com.imglmd.physicsexps.feature.constants.presentation

import com.imglmd.physicsexps.feature.constants.domain.model.Category

interface ConstantsContract {
    data class State(
        val allCategories: List<Category> = emptyList(),
        val search: String = ""
    )

    sealed interface Intent {
        data class ChangeSearchText(val text: String): Intent
    }
}