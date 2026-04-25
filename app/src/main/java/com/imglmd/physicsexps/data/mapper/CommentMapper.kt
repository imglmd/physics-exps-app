package com.imglmd.physicsexps.data.mapper

import com.imglmd.physicsexps.data.database.models.CommentEntity
import com.imglmd.physicsexps.domain.model.Comment

fun CommentEntity.toUi(): Comment {
    return Comment(id = id, experimentRunId = experimentRunId, text = text, createdAt = createdAt)
}

fun Comment.toModel(): CommentEntity {
    return CommentEntity(id = id, experimentRunId = experimentRunId, text = text, createdAt = createdAt)
}