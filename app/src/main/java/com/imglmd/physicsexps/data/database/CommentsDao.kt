package com.imglmd.physicsexps.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.imglmd.physicsexps.data.database.models.CommentEntity
import com.imglmd.physicsexps.data.database.models.ExperimentRunEntity

@Dao
interface CommentsDao {
    @Insert()
    suspend fun insertComment(commentEntity: CommentEntity)
    @Delete
    suspend fun deleteComment(commentEntity: CommentEntity)

    @Query("SELECT * FROM comments WHERE experiment_run_id = :experimentRunId")
    fun getCommentsByRunId(experimentRunId: Int): List<CommentEntity>
}