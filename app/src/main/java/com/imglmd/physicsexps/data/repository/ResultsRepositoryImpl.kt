package com.imglmd.physicsexps.data.repository

import com.imglmd.physicsexps.data.database.ResultsDao
import com.imglmd.physicsexps.data.database.models.ResultEntity
import com.imglmd.physicsexps.domain.model.ExperimentResult
import com.imglmd.physicsexps.domain.repository.ResultsRepository

class ResultsRepositoryImpl(
    private val dao: ResultsDao
) : ResultsRepository {

    override suspend fun insert(result: ExperimentResult): Int {
        return dao.insert(ResultEntity(result = result)).toInt()
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}