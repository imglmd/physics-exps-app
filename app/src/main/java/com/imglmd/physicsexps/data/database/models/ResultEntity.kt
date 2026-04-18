package com.imglmd.physicsexps.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.imglmd.physicsexps.data.database.Converters
import com.imglmd.physicsexps.domain.model.ExperimentResult

@Entity("results")
@TypeConverters(Converters::class)
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val result: ExperimentResult
)