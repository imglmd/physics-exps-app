package com.imglmd.physicsexps.domain.usecase.comment

import com.imglmd.physicsexps.domain.model.Comment
import com.imglmd.physicsexps.domain.repository.CommentRepository

class AddCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(comment: Comment) {
        val commentWithDate = comment.copy(
            createdAt = System.currentTimeMillis()
        )
        commentRepository.insertComment(commentWithDate)
    }
}