package com.imglmd.physicsexps.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.imglmd.physicsexps.data.database.entities.ResultEntity
import com.imglmd.physicsexps.domain.model.InputField
import java.sql.Date

@Entity("experiment_runs",
    foreignKeys = [
        ForeignKey(
            entity = ResultEntity::class,
            parentColumns = ["id"],
            childColumns = ["result_id"]
        ),
        ForeignKey(
            entity = ExperimentEntity::class,
            parentColumns = ["id"],
            childColumns = ["experiment_id"]
        )
    ])
data class ExperimentRunEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("experiment_id")
    val experimentId: Int,
    val date: Date,
    val inputData: String, //TODO() json
    @ColumnInfo("result_id")
    val resultId: Int,
    val status: String
)