package com.imglmd.physicsexps.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imglmd.physicsexps.data.database.models.ResultEntity

@Dao
interface ResultsDao {
    @Insert
    suspend fun insert(entity: ResultEntity): Long

    @Query("DELETE FROM results WHERE run_id = :runId")
    suspend fun deleteByRunId(runId: Int)

    @Query("DELETE FROM results")
    suspend fun deleteAll()

    @Query("SELECT * FROM results WHERE run_id = :runId")
    suspend fun getByRunId(runId: Int): ResultEntity?
}