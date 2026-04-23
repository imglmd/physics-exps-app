package com.imglmd.physicsexps.data.repositoryImpl

import com.imglmd.physicsexps.data.database.ExperimentRunsDao
import com.imglmd.physicsexps.data.mapper.toModel
import com.imglmd.physicsexps.data.mapper.toUi
import com.imglmd.physicsexps.domain.model.ExperimentRun
import kotlinx.coroutines.flow.map

class ExperimentRunRepositoryImpl(private val experimentRunsDao: ExperimentRunsDao) {
    suspend fun getAllExps(): List<ExperimentRun> {
        return experimentRunsDao.getAllExps().map { it.toUi() }
    }
    suspend fun insertExp(experimentRun: ExperimentRun) {
        experimentRunsDao.insertExp(experimentRun.toModel())
    }

    suspend fun deleteExp(experimentRun: ExperimentRun) {
        experimentRunsDao.deleteExp(experimentRun.toModel())
    }

    suspend fun getExpById(id: Int): ExperimentRun {
        return experimentRunsDao.getExpById(id).toUi()
    }
}