package com.imglmd.physicsexps.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val json: String
)