package com.imglmd.physicsexps.feature.constants.presentation

import com.imglmd.physicsexps.feature.constants.domain.model.Category

interface ConstantsContract {
    data class State(
        val allCategories: List<Category> = emptyList(),
        val digits: Int = 0,
        val copyFormat: CopyFormat = CopyFormat.VALUE,
        val search: String = ""
    )

    sealed interface Intent {

        data class ChangeDigits(val value: Int): Intent
        data class ChangeCopyFormat(val format: CopyFormat): Intent
        data class ChangeSearchText(val text: String): Intent
    }
}


enum class CopyFormat {
    VALUE,
    SYMBOL_VALUE,
    FULL
}