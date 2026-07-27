package com.imglmd.feature.history.data.repository

import com.imglmd.feature.history.data.local.ExperimentRunsDao
import com.imglmd.feature.history.data.mapper.toModel
import com.imglmd.feature.history.data.mapper.toUi
import com.imglmd.feature.history.domain.model.ExperimentRun
import com.imglmd.feature.history.domain.repository.ExperimentRunsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class ExperimentRunsRepositoryImpl(
    private val experimentRunsDao: ExperimentRunsDao
): ExperimentRunsRepository {

    override fun getAllExps(): Flow<List<ExperimentRun>> =
        experimentRunsDao.getAllExps().map { list -> list.map { it.toUi() } }

    override fun getLastRuns(limit: Int): Flow<List<ExperimentRun>> =
        experimentRunsDao.getLastRuns(limit).map { list -> list.map { it.toUi() } }


    override suspend fun getExpById(id: Int): ExperimentRun {
        return experimentRunsDao.getExpById(id).toUi()
    }

    override suspend fun insert(experimentRun: ExperimentRun): Int =
        experimentRunsDao.insertExp(experimentRun.toModel()).toInt()

    override suspend fun count(): Int =
        experimentRunsDao.count()

    override suspend fun delete(experimentRun: ExperimentRun) {
        experimentRunsDao.deleteExp(experimentRun.toModel())
    }

    override suspend fun deleteAll() {
        experimentRunsDao.deleteAll()
    }

    override suspend fun deleteOldest(count: Int) {
        experimentRunsDao.deleteOldest(count)
    }
}