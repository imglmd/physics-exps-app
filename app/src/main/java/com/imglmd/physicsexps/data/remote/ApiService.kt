package com.imglmd.physicsexps.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
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
