package com.imglmd.feature.history.domain.repository

import com.imglmd.feature.history.domain.model.ExperimentRun
import kotlinx.coroutines.flow.Flow

interface ExperimentRunsRepository {
    fun getAllExps(): Flow<List<ExperimentRun>>
    fun getLastRuns(limit: Int): Flow<List<ExperimentRun>>

    suspend fun getExpById(id: Int): ExperimentRun

    suspend fun insert(experimentRun: ExperimentRun): Int

    suspend fun count(): Int

    suspend fun delete(experimentRun: ExperimentRun)
    suspend fun deleteAll()
    suspend fun deleteOldest(count: Int)
}