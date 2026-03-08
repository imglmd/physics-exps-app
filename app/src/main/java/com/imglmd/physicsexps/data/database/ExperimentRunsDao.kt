package com.imglmd.physicsexps.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imglmd.physicsexps.data.database.entities.ExperimentEntity
import com.imglmd.physicsexps.data.database.entities.ExperimentRunEntity
import kotlinx.coroutines.flow.Flow

interface ExperimentRunsDao {
    @Insert()
    suspend fun insertItem(experimentRunEntity: ExperimentRunEntity)
    @Delete
    suspend fun deleteItem(experimentRunEntity: ExperimentRunEntity)

    @Query("SELECT * FROM experiment_runs")
    fun getAllItems(): Flow<List<ExperimentRunEntity>>

    @Query("SELECT * FROM experiment_runs WHERE id = :id")
    fun getById(id: Int): Flow<ExperimentRunEntity>
}