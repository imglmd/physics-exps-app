package com.imglmd.feature.experiment.domain.usecase.comment

import com.imglmd.feature.experiment.domain.model.Comment
import com.imglmd.feature.experiment.domain.repository.CommentRepository

class GetCommentsUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(runId: Int): List<Comment> {
        return commentRepository.getCommentsByRunId(runId)
    }
}