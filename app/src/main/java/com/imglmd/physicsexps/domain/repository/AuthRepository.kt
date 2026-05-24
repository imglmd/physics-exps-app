package com.imglmd.physicsexps.domain.repository

interface AuthRepository {
    suspend fun registerDevice(deviceId: String, deviceName: String)

    fun getToken(): String?

    fun hasToken(): Boolean
}