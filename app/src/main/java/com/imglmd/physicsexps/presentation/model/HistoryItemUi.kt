package com.imglmd.physicsexps.presentation.model

import com.imglmd.physicsexps.domain.model.PhysicalQuantity


data class HistoryItemUi(
    val id: Int,
    val experimentName: String,
    val category: String,
    val date: Long,
    val resultId: Int,
    val inputs: Map<String, Double> = emptyMap(),
    val points: List<Pair<Double, Double>> = emptyList(),
    val quantities: List<PhysicalQuantity> = emptyList(),
)
