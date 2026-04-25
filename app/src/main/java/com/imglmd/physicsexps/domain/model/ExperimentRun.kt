package com.imglmd.physicsexps.domain.model

import java.util.Date


data class ExperimentRun (
    val id: Int,
    val experimentId: String,
    val date: Long,
    val inputData: String,
    val resultId: Int,
)