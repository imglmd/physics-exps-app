package com.imglmd.feature.experiment.data.repository

import com.imglmd.feature.experiment.data.local.CommentsDao
import com.imglmd.feature.experiment.data.mapper.toModel
import com.imglmd.feature.experiment.data.mapper.toUi
import com.imglmd.feature.experiment.domain.model.Comment
import com.imglmd.feature.experiment.domain.repository.CommentRepository

class CommentRepositoryImpl(
    private val commentsDao: CommentsDao
): CommentRepository {
    override suspend fun getCommentsByRunId(runId: Int): List<Comment> {
        return commentsDao.getCommentsByRunId(runId).map { it.toUi() }
    }

    override suspend fun insertComment(comment: Comment) {
        commentsDao.insertComment(comment.toModel())
    }

    override suspend fun deleteComment(id: Int) {
        commentsDao.deleteComment(id)
    }
}
