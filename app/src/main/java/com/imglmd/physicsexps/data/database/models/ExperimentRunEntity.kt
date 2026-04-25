package com.imglmd.physicsexps.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("experiment_runs",
    foreignKeys = [
        ForeignKey(
            entity = ResultEntity::class,
            parentColumns = ["id"],
            childColumns = ["result_id"]
        )
    ])
data class ExperimentRunEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("experiment_id")
    val experimentId: String,
    val date: Long,
    val inputData: String,
    @ColumnInfo("result_id")
    val resultId: Int,
)