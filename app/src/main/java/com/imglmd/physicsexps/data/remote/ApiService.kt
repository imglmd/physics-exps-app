package com.imglmd.physicsexps.data.remote

import com.imglmd.physicsexps.data.remote.dto.ExperimentImagesDto
import com.imglmd.physicsexps.data.remote.dto.ExperimentPreviewDto
import com.imglmd.physicsexps.data.remote.dto.MediaDto
import com.imglmd.physicsexps.data.remote.dto.MediaListDto
import com.imglmd.physicsexps.data.remote.dto.MediaTempUrlDto
import com.imglmd.physicsexps.data.remote.dto.RegisterRequestDto
import com.imglmd.physicsexps.data.remote.dto.RegisterResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/register-device")
    suspend fun registerDevice(
        @Body request: RegisterRequestDto
    ): RegisterResponseDto

    @GET("/api/media-temp-url")
    suspend fun getMediaTempUrl(
        @Query("path") path: String
    ): MediaTempUrlDto
    @GET("/api/experiments")
    suspend fun getExperiments(): List<ExperimentPreviewDto>

    @GET("/api/experiments/{experiment_id}/images")
    suspend fun getExperimentImages(
        @Path("experiment_id") experimentId: String
    ): ExperimentImagesDto

    @GET("/api/experiment_runs/{run_id}/media")
    suspend fun getMedia(@Path("run_id") runId: String): MediaListDto

    @Multipart
    @POST("/api/experiment_runs/{run_id}/media")
    suspend fun uploadMedia(
        @Part media: MultipartBody.Part,
        @Path("run_id") runId: String,
        @Part("fileName") fileName: RequestBody
    ): MediaDto

    @DELETE("/api/experiment_runs/{run_id}/media/{media_id}")
    suspend fun deleteMedia(@Path("run_id") runId: String, @Path("media_id") id: String)
}
