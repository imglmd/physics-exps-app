package com.imglmd.feature.experiment.data.remote

import com.imglmd.core.auth.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

//todo перенести в core:network
class AuthInterceptor(
    private val tokenStorage: TokenStorage
): Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val token = tokenStorage.getToken()

        val request = chain.request()
            .newBuilder()
            .apply {
                if (!token.isNullOrBlank()) { header("Authorization", "Bearer $token") }
            }
            .build()

        return chain.proceed(request)
    }
}