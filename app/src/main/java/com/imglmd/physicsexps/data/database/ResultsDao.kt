package com.imglmd.physicsexps.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imglmd.physicsexps.data.database.models.ResultEntity

@Dao
interface ResultsDao {
    @Insert
    suspend fun insert(entity: ResultEntity): Long

    @Query("DELETE FROM results WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM results WHERE id = :id")
    suspend fun getById(id: Int): ResultEntity?
}