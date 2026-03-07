package com.imglmd.physicsexps.data

import com.imglmd.physicsexps.domain.model.ExperimentResult

class InMemoryResultRepository {
    private var cached: ExperimentResult? = null

    fun save(result: ExperimentResult) { cached = result }
    fun get(): ExperimentResult? = cached
    fun clear() { cached = null }
}