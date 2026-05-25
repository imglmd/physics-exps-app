package com.imglmd.physicsexps.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity("experiment_runs")
data class ExperimentRunEntity(
    @PrimaryKey
    val id: Int = Random.nextInt(),
    @ColumnInfo("experiment_id")
    val experimentId: String,
    val date: Long,
    val inputData: String,
)