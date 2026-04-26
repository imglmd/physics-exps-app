package com.imglmd.physicsexps.domain.model

data class ExperimentResult(
    val experiment: Experiment,
    val quantities: List<PhysicalQuantity>, //все величины
    val points: List<Pair<Double, Double>>, // точки для графиков
    val xLabel: String,
    val yLabel: String,
    val date: Long,
    val inputs: Map<String, Double> = emptyMap()
)
