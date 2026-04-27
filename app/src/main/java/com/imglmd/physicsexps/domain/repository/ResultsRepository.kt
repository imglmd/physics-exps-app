package com.imglmd.physicsexps.domain.repository

import com.imglmd.physicsexps.domain.model.ExperimentResult

interface ResultsRepository {
    suspend fun getByRunId(runId: Int): ExperimentResult?
    suspend fun insert(runId: Int, result: ExperimentResult): Int
    suspend fun deleteAll()
}