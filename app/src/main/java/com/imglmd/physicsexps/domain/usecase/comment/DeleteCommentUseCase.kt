package com.imglmd.physicsexps.domain.usecase.comment

import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.repository.CommentRepository

class DeleteCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend fun invoke(comment: Comment) {
        commentRepository.deleteComment(comment)
    }
}