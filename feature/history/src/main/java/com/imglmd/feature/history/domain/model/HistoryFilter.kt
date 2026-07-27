package com.imglmd.feature.history.domain.model

data class HistoryFilter(
    val experimentId: String? = null,
    val dateFrom: Long? = null,
    val dateTo: Long? = null,
    val sortOrder: SortOrder = SortOrder.DATE_DESC
)