package com.imglmd.physicsexps.domain.model

import java.util.Date


data class ExperimentRun (
    val id: Int,
    val experimentId: Int,
    val date: Long,
    val inputData: String,
    val resultId: Int,
    val status: String
)