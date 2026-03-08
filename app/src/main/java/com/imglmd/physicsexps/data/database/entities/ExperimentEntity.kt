package com.imglmd.physicsexps.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("experiments")
data class ExperimentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val category: String
)