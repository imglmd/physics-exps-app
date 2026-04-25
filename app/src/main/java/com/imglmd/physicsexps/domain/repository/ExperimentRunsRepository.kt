package com.imglmd.physicsexps.domain.repository

import com.imglmd.physicsexps.domain.model.ExperimentRun
import kotlinx.coroutines.flow.Flow

interface ExperimentRunsRepository {
    fun getAllExps(): Flow<List<ExperimentRun>>

    suspend fun getExpById(id: Int): ExperimentRun

    suspend fun insert(experimentRun: ExperimentRun): Int

    suspend fun delete(experimentRun: ExperimentRun)
}