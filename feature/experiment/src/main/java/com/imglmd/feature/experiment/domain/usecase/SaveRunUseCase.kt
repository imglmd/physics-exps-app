package com.imglmd.feature.experiment.domain.usecase

import com.imglmd.core.experiments.model.ExperimentResult
import com.imglmd.feature.experiment.domain.model.ExperimentRun
import com.imglmd.feature.experiment.domain.repository.ExperimentRunsRepository
import com.imglmd.feature.experiment.domain.repository.ResultsRepository
import com.imglmd.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.util.UUID

class SaveRunUseCase(
    private val runsRepository: ExperimentRunsRepository,
    private val resultsRepository: ResultsRepository,
    private val settingsRepository: SettingsRepository
) {

    private val json = Json

    suspend operator fun invoke(
        result: ExperimentResult,
        inputs: Map<String, Double>
    ): Int {

        val runId = runsRepository.insert(
            ExperimentRun(
                remoteId = UUID.randomUUID().toString(),
                experimentId = result.experimentId,
                date = System.currentTimeMillis(),
                inputData = json.encodeToString(inputs),
            )
        )

        resultsRepository.insert(runId, result)

        val limit = settingsRepository.settings.first().maxHistoryEntries

        if (limit != null){
            val count = runsRepository.count()
            if (count > limit) {
                runsRepository.deleteOldest(count - limit)
            }
        }
        return runId
    }
}