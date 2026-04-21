package com.imglmd.physicsexps.domain.validation

sealed class ValidationResult {
    data class Success(val values: Map<String, Double>): ValidationResult()
    data class Error(val errors: List<ValidationError>): ValidationResult()
}