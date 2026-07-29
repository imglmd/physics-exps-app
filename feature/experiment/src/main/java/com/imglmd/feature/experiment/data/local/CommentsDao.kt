package com.imglmd.feature.experiment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imglmd.feature.experiment.data.local.models.CommentEntity

@Dao
interface CommentsDao {
    @Insert()
    suspend fun insertComment(commentEntity: CommentEntity)
    @Query("DELETE FROM comments WHERE id = :id")
    suspend fun deleteComment(id: Int)

    @Query("SELECT * FROM comments WHERE experiment_run_id = :experimentRunId ORDER BY created_at DESC")
    fun getCommentsByRunId(experimentRunId: Int): List<CommentEntity>
}