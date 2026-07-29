package com.imglmd.feature.experiment.domain.repository

import com.imglmd.core.experiments.model.ExperimentResult


interface ResultsRepository {
    suspend fun getByRunId(runId: Int): ExperimentResult?
    suspend fun insert(runId: Int, result: ExperimentResult): Int
    suspend fun deleteAll()
}