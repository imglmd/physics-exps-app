package com.imglmd.physicsexps.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("experiment_runs")
data class ExperimentRunEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo("remote_id")
    val remoteId: String = UUID.randomUUID().toString(),

    @ColumnInfo("experiment_id")
    val experimentId: String,

    val date: Long,
    val inputData: String,
)