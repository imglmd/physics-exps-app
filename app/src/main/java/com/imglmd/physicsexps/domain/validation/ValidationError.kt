package com.imglmd.physicsexps.domain.validation

sealed class ValidationError {

    object NotEnoughInputs: ValidationError()

    data class InvalidNumber(val fieldKey: String): ValidationError()

    data class OutOfRange(
        val fieldKey: String,
        val min: Double? = null,
        val max: Double? = null
    ): ValidationError()

    data class RequiredField(
        val fieldKey: String
    ): ValidationError()
}