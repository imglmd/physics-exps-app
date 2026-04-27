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
    suspend fun insertExp(experimentRunEntity: ExperimentRunEntity): Long
    @Delete
    suspend fun deleteExp(experimentRunEntity: ExperimentRunEntity)

    @Query("DELETE FROM experiment_runs")
    suspend fun deleteAll()

    @Query("SELECT * FROM experiment_runs ORDER BY date DESC")
    fun getAllExps(): Flow<List<ExperimentRunEntity>>

    @Query("SELECT * FROM experiment_runs ORDER BY date DESC LIMIT :limit")
    fun getLastRuns(limit: Int): Flow<List<ExperimentRunEntity>>
    @Query("SELECT * FROM experiment_runs WHERE id = :id")
    suspend fun getExpById(id: Int): ExperimentRunEntity
}