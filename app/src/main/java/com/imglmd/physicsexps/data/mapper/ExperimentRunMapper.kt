package com.imglmd.physicsexps.data.mapper

import com.imglmd.physicsexps.data.database.models.ExperimentRunEntity
import com.imglmd.physicsexps.domain.model.ExperimentRun

fun ExperimentRunEntity.toUi(): ExperimentRun {
    return ExperimentRun(id = id, experimentId = experimentId, date = date,
        inputData = inputData)
}

fun ExperimentRun.toModel(): ExperimentRunEntity {
    return ExperimentRunEntity(id = id, experimentId = experimentId, date = date,
        inputData = inputData)
}