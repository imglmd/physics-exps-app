package com.imglmd.physicsexps.presentation.screens.constants

import com.imglmd.physicsexps.domain.model.Category

interface ConstantsContract {
    data class State(
        val allCategories: List<Category> = emptyList(),
        val search: String = ""
    )

    sealed interface Intent {
        data class ChangeSearchText(val text: String): Intent
    }
}