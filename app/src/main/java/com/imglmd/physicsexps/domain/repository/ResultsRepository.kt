package com.imglmd.physicsexps.domain.repository

import com.imglmd.physicsexps.domain.model.ExperimentResult

interface ResultsRepository {
    suspend fun getById(id: Int): ExperimentResult?
    suspend fun insert(result: ExperimentResult): Int
    suspend fun deleteById(id: Int)
}