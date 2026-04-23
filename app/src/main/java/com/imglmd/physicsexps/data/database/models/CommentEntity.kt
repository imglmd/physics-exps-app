package com.imglmd.physicsexps.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("comments",
    foreignKeys = [
        ForeignKey(
            entity = ExperimentRunEntity::class,
            parentColumns = ["id"],
            childColumns = ["experiment_run_id"]
        )
    ])
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String,
    @ColumnInfo("experiment_run_id")
    val experimentRunId: Int
)