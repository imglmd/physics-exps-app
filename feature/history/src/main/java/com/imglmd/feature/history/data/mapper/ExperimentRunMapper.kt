package com.imglmd.feature.history.data.mapper

import com.imglmd.feature.history.data.local.ExperimentRunEntity
import com.imglmd.feature.history.domain.model.ExperimentRun

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