package com.imglmd.physicsexps.domain.model

data class ExperimentResult(
    val experiment: Experiment,
    val quantities: List<PhysicalQuantity>, //все величины
    val points: List<Pair<Double, Double>>?, // точки для графиков, null если графика нет
    val xLabel: String?,
    val yLabel: String?,
    val date: String
)
