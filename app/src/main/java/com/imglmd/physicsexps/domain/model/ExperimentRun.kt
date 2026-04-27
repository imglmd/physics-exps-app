package com.imglmd.physicsexps.domain.model


data class ExperimentRun (
    val id: Int = 0,
    val experimentId: String,
    val date: Long,
    val inputData: String,
)