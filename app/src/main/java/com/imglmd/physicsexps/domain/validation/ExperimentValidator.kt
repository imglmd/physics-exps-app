package com.imglmd.physicsexps.domain.validation

import com.imglmd.physicsexps.domain.model.Experiment

class ExperimentValidator {

    fun validate(
        experiment: Experiment,
        rawInputs: Map<String, String>
    ): ValidationResult {

        val parsedInputs = mutableMapOf<String, Double>()
        val errors = mutableListOf<ValidationError>()

        val allFields = experiment.inputFields + experiment.additionalInputFields

        for (field in allFields) {

            val rawValue = rawInputs[field.key]

            if (field.required && rawValue.isNullOrBlank()) {
                errors += ValidationError.RequiredField(field.key)
                continue
            }

            if (rawValue.isNullOrBlank()) continue

            val number = rawValue.toDoubleOrNull()

            if (number == null) {
                errors += ValidationError.InvalidNumber(field.key)
                continue
            }

            if (field.min != null && number < field.min) {
                errors += ValidationError.OutOfRange(field.key, min = field.min)
                continue
            }

            if (field.max != null && number > field.max) {
                errors += ValidationError.OutOfRange(field.key, max = field.max)
                continue
            }

            parsedInputs[field.key] = number
        }

        // ошибки базовой валидации
        if (errors.isNotEmpty()) {
            return ValidationResult.Error(errors)
        }

        if (experiment.id == "harmonic_vibrations") {
            val amp = parsedInputs["amplitude"]!!
            val start = parsedInputs["start_position"]!!

            if (start > amp) {
                errors += ValidationError.InvalidNumber("start_position")
            }
        }

        if (errors.isNotEmpty()) {
            return ValidationResult.Error(errors)
        }

        // дальше эксперимент сам решает
        return experiment.validateInputs(parsedInputs)
    }
}