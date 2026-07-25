package com.imglmd.core.auth

interface AuthRepository {
    suspend fun registerDevice(deviceId: String, deviceName: String)

    fun getToken(): String?

    fun hasToken(): Boolean
}