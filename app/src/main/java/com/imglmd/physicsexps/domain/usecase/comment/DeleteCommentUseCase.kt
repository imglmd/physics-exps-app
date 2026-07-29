package com.imglmd.physicsexps.domain.usecase.comment

import com.imglmd.feature.experiment.domain.repository.CommentRepository

class DeleteCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(id: Int) {
        commentRepository.deleteComment(id)
    }
}