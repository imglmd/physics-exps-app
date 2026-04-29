package com.imglmd.physicsexps.data

import com.imglmd.physicsexps.domain.model.ExperimentResult


data class ResultBundle(
    val result: ExperimentResult,
    val inputs: Map<String, Double>,
    val replaceRunId: Int? = null // айди старого run, который нужно удалить после сохранения нового
)

class InMemoryResultRepository {
    private var bundle: ResultBundle? = null

    fun save(
        result: ExperimentResult,
        inputs: Map<String, Double>,
        replaceRunId: Int? = null
    ) {
        bundle = ResultBundle(result, inputs, replaceRunId)
    }

    fun get(): ResultBundle? = bundle
    fun clear() { bundle = null }
}