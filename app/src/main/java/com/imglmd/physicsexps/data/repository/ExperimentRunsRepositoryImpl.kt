package com.imglmd.physicsexps.data.repository

import com.imglmd.physicsexps.data.database.ExperimentRunsDao
import com.imglmd.physicsexps.data.mapper.toModel
import com.imglmd.physicsexps.data.mapper.toUi
import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository

class ExperimentRunsRepositoryImpl(
    private val experimentRunsDao: ExperimentRunsDao
): ExperimentRunsRepository {

    override suspend fun getAllExps(): List<ExperimentRun> {
        return experimentRunsDao.getAllExps().map { it.toUi() }
    }

    override suspend fun getExpById(id: Int): ExperimentRun {
        return experimentRunsDao.getExpById(id).toUi()
    }

    override suspend fun insert(experimentRun: ExperimentRun) {
        experimentRunsDao.insertExp(experimentRun.toModel())
    }

    override suspend fun delete(experimentRun: ExperimentRun) {
        experimentRunsDao.deleteExp(experimentRun.toModel())
    }
}