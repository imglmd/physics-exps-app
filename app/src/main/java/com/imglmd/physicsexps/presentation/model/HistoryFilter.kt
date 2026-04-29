package com.imglmd.physicsexps.presentation.model

data class HistoryFilter(
    val experimentId: String? = null,
    val dateFrom: Long? = null,
    val dateTo: Long? = null,
    val sortOrder: SortOrder = SortOrder.DATE_DESC
)

enum class SortOrder{
    DATE_DESC, //новые сверху
    DATE_ASC, // старые сверху
    EXPERIMENT // по алфавиту
}