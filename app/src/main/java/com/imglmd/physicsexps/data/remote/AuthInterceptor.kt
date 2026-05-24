package com.imglmd.physicsexps.data.remote

import com.imglmd.physicsexps.data.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

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