package com.imglmd.physicsexps.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.imglmd.physicsexps.data.database.models.ExperimentRunEntity
import kotlinx.coroutines.flow.Flow
@Dao()
interface ExperimentRunsDao {
    @Insert()
    suspend fun insertExp(experimentRunEntity: ExperimentRunEntity)
    @Delete
    suspend fun deleteExp(experimentRunEntity: ExperimentRunEntity)

    @Query("SELECT * FROM experiment_runs")
    fun getAllExps(): List<ExperimentRunEntity>

    @Query("SELECT * FROM experiment_runs WHERE id = :id")
    fun getExpById(id: Int): ExperimentRunEntity
}