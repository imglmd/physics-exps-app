package com.imglmd.physicsexps.domain.repository

interface ExperimentMediaRepository {
    suspend fun getPreviewUrls(): Map<String, String>
    suspend fun getImageUrls(experimentId: String): List<String>
}
