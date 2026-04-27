package com.imglmd.physicsexps.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExperimentResult(
    val experimentId: String,
    val quantities: List<PhysicalQuantity>, //все величины
    val points: List<Pair<Double, Double>>, // точки для графиков
    val xLabel: String,
    val yLabel: String,
    val date: Long,
)
