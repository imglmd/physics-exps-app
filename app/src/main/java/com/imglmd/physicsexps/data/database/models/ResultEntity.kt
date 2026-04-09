package com.imglmd.physicsexps.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imglmd.physicsexps.domain.model.ExperimentResult

@Entity("results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val result: ExperimentResult
)