package com.imglmd.feature.experiment.data.mapper

import com.imglmd.feature.experiment.data.local.ExperimentRunEntity
import com.imglmd.feature.experiment.domain.model.ExperimentRun

fun ExperimentRunEntity.toUi(): ExperimentRun {
    return ExperimentRun(
        id = id,
        remoteId = remoteId,
        experimentId = experimentId,
        date = date,
        inputData = inputData
    )
}

fun ExperimentRun.toModel(): ExperimentRunEntity {
    return ExperimentRunEntity(
        id = id,
        remoteId = remoteId,
        experimentId = experimentId,
        date = date,
        inputData = inputData
    )
}