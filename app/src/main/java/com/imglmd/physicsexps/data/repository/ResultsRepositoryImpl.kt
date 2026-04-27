package com.imglmd.physicsexps.data.repository

import com.imglmd.physicsexps.data.database.ResultsDao
import com.imglmd.physicsexps.data.database.models.ResultEntity
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.repository.ResultsRepository
import kotlinx.serialization.json.Json

class ResultsRepositoryImpl(
    private val dao: ResultsDao
) : ResultsRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getByRunId(runId: Int): ExperimentResult? {
        return dao.getByRunId(runId)?.let {
            json.decodeFromString<ExperimentResult>(it.json)
        }
    }

    override suspend fun insert(runId: Int, result: ExperimentResult): Int {
        return dao.insert(
            ResultEntity(
                runId = runId,
                json = json.encodeToString(result)
            )
        ).toInt()
    }

    override suspend fun deleteByRunId(runId: Int) {
        dao.deleteByRunId(runId)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}