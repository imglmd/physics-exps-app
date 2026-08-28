package com.imglmd.feature.experiment.data.mapper

import com.imglmd.feature.experiment.data.local.models.CommentEntity
import com.imglmd.feature.experiment.domain.model.Comment

fun CommentEntity.toUi(): Comment {
    return Comment(id = id, experimentRunId = experimentRunId, text = text, createdAt = createdAt)
}

fun Comment.toModel(): CommentEntity {
    return CommentEntity(id = id, experimentRunId = experimentRunId, text = text, createdAt = createdAt)
}