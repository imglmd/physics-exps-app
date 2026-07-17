package com.imglmd.physicsexps.feature.constants.presentation

data class ConstantsPreferences(
    val digits: Int = 5,
    val copyMode: CopyMode = CopyMode.VALUE
)


enum class CopyMode {
    VALUE,
    SYMBOL_VALUE,
    FULL
}