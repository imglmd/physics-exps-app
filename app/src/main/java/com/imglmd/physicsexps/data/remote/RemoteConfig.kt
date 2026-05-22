package com.imglmd.physicsexps.data.remote

class RemoteConfig(
    baseUrl: String,
    val apiKey: String
) {
    val baseUrl: String = normalizeBaseUrl(baseUrl)

    fun resolveUrl(rawUrl: String): String {
        if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) {
            return rawUrl
        }

        return baseUrl + rawUrl.removePrefix("/")
    }

    private fun normalizeBaseUrl(value: String): String {
        val trimmed = value.trim()
        require(trimmed.isNotEmpty()) { "BACKEND_BASE_URL is empty" }
        require(trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            "BACKEND_BASE_URL must start with http:// or https://"
        }
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }
}
