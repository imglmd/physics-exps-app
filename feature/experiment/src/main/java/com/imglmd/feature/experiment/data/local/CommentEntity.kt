package com.imglmd.feature.experiment.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.imglmd.feature.experiment.data.local.ExperimentRunEntity

@Entity("comments",
    foreignKeys = [
        ForeignKey(
            entity = ExperimentRunEntity::class,
            parentColumns = ["id"],
            childColumns = ["experiment_run_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    @ColumnInfo("experiment_run_id",  index = true)
    val experimentRunId: Int,
    @ColumnInfo("created_at")
    val createdAt: Long
)