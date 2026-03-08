package com.imglmd.physicsexps.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val result: String //TODO() json
)