package com.imglmd.physicsexps.data.mapper

import com.imglmd.physicsexps.data.database.models.ResultEntity
import com.imglmd.physicsexps.domain.model.ExperimentResult

fun ExperimentResult.toEntity() = ResultEntity(result = this)