package com.imglmd.feature.experiment.domain.repository

interface ExperimentMediaRepository {
    suspend fun getPreviewUrls(): Map<String, String>
    suspend fun getImageUrls(experimentId: String): List<String>
}
