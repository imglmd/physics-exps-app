package com.imglmd.physicsexps.data.repository

import com.imglmd.physicsexps.data.database.ResultsDao
import com.imglmd.physicsexps.data.database.models.ResultEntity
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.repository.ResultsRepository
import kotlinx.serialization.json.Json

class ResultsRepositoryImpl(
    private val dao: ResultsDao
) : ResultsRepository {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun getById(id: Int): ExperimentResult? {
        return dao.getById(id)?.let {
            json.decodeFromString<ExperimentResult>(it.json)
        }
    }

    override suspend fun insert(result: ExperimentResult): Int {
        return dao.insert(
            ResultEntity(
                json = json.encodeToString(result)
            )
        ).toInt()
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}