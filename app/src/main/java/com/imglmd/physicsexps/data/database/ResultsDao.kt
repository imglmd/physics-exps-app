package com.imglmd.physicsexps.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imglmd.physicsexps.data.database.models.ResultEntity

@Dao
interface ResultsDao {
    @Insert
    suspend fun insert(entity: ResultEntity): Long

    //deleteByRunId не нужен тк result удаляеться вместе с run

    @Query("DELETE FROM results")
    suspend fun deleteAll()

    @Query("SELECT * FROM results WHERE run_id = :runId")
    suspend fun getByRunId(runId: Int): ResultEntity?
}