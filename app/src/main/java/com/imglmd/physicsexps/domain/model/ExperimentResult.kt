package com.imglmd.physicsexps.domain.model

data class ExperimentResult(
    val experiment: Experiment,
    val quantities: List<PhysicalQuantity>,   //все величины
)
