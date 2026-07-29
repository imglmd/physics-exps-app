package com.imglmd.feature.experiment.domain.usecase.comment

import com.imglmd.feature.experiment.domain.model.Comment
import com.imglmd.feature.experiment.domain.repository.CommentRepository

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