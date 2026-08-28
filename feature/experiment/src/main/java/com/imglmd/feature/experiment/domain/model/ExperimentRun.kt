package com.imglmd.feature.experiment.domain.model

data class ExperimentRun (
    val id: Int = 0,
    val remoteId: String,
    val experimentId: String,
    val date: Long,
    val inputData: String,
)