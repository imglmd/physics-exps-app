package com.imglmd.physicsexps.feature.constants.presentation.util

import com.imglmd.physicsexps.feature.constants.domain.model.NumberValue
import java.math.BigDecimal
import java.math.MathContext
import java.util.Locale
import kotlin.math.abs

private val superscriptDigits = mapOf(
    '0' to '⁰', '1' to '¹', '2' to '²', '3' to '³', '4' to '⁴',
    '5' to '⁵', '6' to '⁶', '7' to '⁷', '8' to '⁸', '9' to '⁹', '-' to '⁻'
)

private fun Int.toSuperscript(): String =
    toString().map { superscriptDigits[it] ?: it }.joinToString("")

fun Double.formatWithDigits(digits: Int): String {
    if (this == 0.0) return "0"
    val abs = abs(this)

    return if (abs in 1.0e-3..1.0e6) {
        BigDecimal(this)
            .round(MathContext(digits.coerceAtLeast(1)))
            .stripTrailingZeros()
            .toPlainString()
    } else {
        val raw = String.format(Locale.US, "%.${(digits - 1).coerceAtLeast(0)}e", this)
        val (mantissaRaw, expRaw) = raw.split("e")
        val mantissa = mantissaRaw.trimEnd('0').trimEnd('.')
        val exponent = expRaw.toInt()
        "$mantissa×10${exponent.toSuperscript()}"
    }
}

fun NumberValue.format(digits: Int): String = when (this) {
    is NumberValue.Single -> value.formatWithDigits(digits)
    is NumberValue.Range -> "${from.formatWithDigits(digits)}–${to.formatWithDigits(digits)}"
}


