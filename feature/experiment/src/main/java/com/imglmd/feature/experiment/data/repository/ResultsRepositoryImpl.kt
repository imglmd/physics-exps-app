package com.imglmd.feature.experiment.data.repository

import com.imglmd.feature.experiment.data.local.ResultEntity
import com.imglmd.feature.experiment.data.local.ResultsDao
import com.imglmd.feature.experiment.domain.repository.ResultsRepository
import com.imglmd.core.experiments.model.ExperimentResult
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


    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}