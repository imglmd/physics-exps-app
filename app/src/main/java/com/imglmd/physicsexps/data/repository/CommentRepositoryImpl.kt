package com.imglmd.physicsexps.data.repositoryImpl

import com.imglmd.physicsexps.data.database.CommentsDao
import com.imglmd.physicsexps.data.mapper.toModel
import com.imglmd.physicsexps.data.mapper.toUi
import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.repository.CommentRepository

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