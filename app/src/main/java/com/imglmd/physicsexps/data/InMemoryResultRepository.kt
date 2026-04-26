package com.imglmd.physicsexps.data

import com.imglmd.physicsexps.domain.model.ExperimentResult


data class ResultBundle(
    val result: ExperimentResult,
    val inputs: Map<String, Double>
)

class InMemoryResultRepository {

    private var bundle: ResultBundle? = null

    fun save(result: ExperimentResult, inputs: Map<String, Double>) {
        bundle = ResultBundle(result, inputs)
    }

    fun get(): ResultBundle? = bundle

    fun clear() {
        bundle = null
    }
}