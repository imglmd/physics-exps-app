package com.imglmd.physicsexps.domain.repository

import com.imglmd.physicsexps.domain.model.Comment

interface CommentRepository {
    suspend fun getCommentsByRunId(runId: Int):List<Comment>

    suspend fun insertComment(comment: Comment)

    suspend fun deleteComment(comment: Comment)
}