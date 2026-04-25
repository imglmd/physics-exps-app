package com.imglmd.physicsexps.domain.usecase

import com.imglmd.physicsexps.data.repositoryImpl.CommentRepositoryImpl
import com.imglmd.physicsexps.domain.model.Comment

class GetCommentsUseCase(
    private val commentRepositoryImpl: CommentRepositoryImpl
) {
    suspend fun invoke(runId: Int): List<Comment> {
        return commentRepositoryImpl.getCommentsByRunId(runId)
    }
}