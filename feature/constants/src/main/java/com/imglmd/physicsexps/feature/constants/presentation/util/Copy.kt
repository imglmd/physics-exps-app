package com.imglmd.physicsexps.feature.constants.presentation.util

import com.imglmd.physicsexps.feature.constants.domain.model.Constant
import com.imglmd.physicsexps.feature.constants.presentation.CopyMode

fun buildCopyText(
    constant: Constant,
    formattedValue: String,
    unitText: String,
    copyMode: CopyMode
): String = when (copyMode) {
    CopyMode.VALUE -> formattedValue
    CopyMode.SYMBOL_VALUE -> "${constant.symbol} = $formattedValue"
    CopyMode.FULL -> buildString {
        append(constant.symbol)
        append(" = ")
        append(formattedValue)
        if (unitText.isNotBlank()) {
            append(" ")
            append(unitText)
        }
    }
}