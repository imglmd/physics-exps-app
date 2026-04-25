package com.imglmd.physicsexps.data.mapper

import com.imglmd.physicsexps.data.database.models.ResultEntity
import com.imglmd.physicsexps.domain.model.ExperimentResult

fun ExperimentResult.toEntity() = ResultEntity(result = this)
fun ResultEntity.toDomain() = ExperimentResult(
    experiment = this.result.experiment,
    quantities = this.result.quantities,
    points = this.result.points,
    xLabel = this.result.xLabel,
    yLabel = this.result.yLabel,
    date = this.result.date
)