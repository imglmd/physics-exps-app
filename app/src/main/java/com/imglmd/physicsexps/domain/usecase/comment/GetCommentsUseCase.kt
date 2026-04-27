package com.imglmd.physicsexps.domain.usecase.comment

import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.repository.CommentRepository

class GetCommentsUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(runId: Int): List<Comment> {
        return commentRepository.getCommentsByRunId(runId)
    }
}