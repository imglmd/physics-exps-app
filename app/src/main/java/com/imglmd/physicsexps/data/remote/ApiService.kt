package com.imglmd.physicsexps.data.remote

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("")
    suspend fun getMedia(@Query("run_id")runId: Int): List<MediaDto>

    //TODO() медиа с устройства
    @POST("")
    suspend fun postMedia(): MediaDto
}