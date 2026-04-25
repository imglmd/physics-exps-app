package com.imglmd.physicsexps.domain.usecase

import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.repository.CommentRepository

class InsertCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend fun invoke(comment: Comment) {
        commentRepository.insertComment(comment)
    }
}