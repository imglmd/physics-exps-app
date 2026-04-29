package com.imglmd.physicsexps.domain.usecase.run

import com.imglmd.physicsexps.domain.model.ExperimentRun
import com.imglmd.physicsexps.domain.repository.ExperimentRunsRepository
import com.imglmd.physicsexps.domain.usecase.experiment.GetExperimentByIdUseCase
import com.imglmd.physicsexps.presentation.model.HistoryFilter
import com.imglmd.physicsexps.presentation.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class GetFilteredRunsUseCase(
    private val repository: ExperimentRunsRepository,
    private val getExperiment: GetExperimentByIdUseCase
) {
    operator fun invoke(filterFlow: StateFlow<HistoryFilter>): Flow<List<ExperimentRun>> =
        repository.getAllExps()
            .combine(filterFlow) { runs, filter -> applyFilter(runs, filter) }

    private fun applyFilter(runs: List<ExperimentRun>, filter: HistoryFilter): List<ExperimentRun>{
        return runs
            .filter { run ->
                filter.experimentId == null || run.experimentId == filter.experimentId
            }
            .filter { run ->
                filter.dateFrom == null || run.date >= filter.dateFrom
            }
            .filter { run ->
                filter.dateTo == null || run.date <= filter.dateTo
            }
            .let { filtered ->
                when (filter.sortOrder) {
                    SortOrder.DATE_DESC  -> filtered.sortedByDescending { it.date }
                    SortOrder.DATE_ASC   -> filtered.sortedBy { it.date }
                    SortOrder.EXPERIMENT -> filtered.sortedBy {
                        getExperiment(it.experimentId).name
                    }
                }
            }
    }
}