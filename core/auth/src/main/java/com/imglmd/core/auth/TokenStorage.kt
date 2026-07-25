package com.imglmd.core.auth

import android.content.Context
import androidx.core.content.edit

class TokenStorage(
    private val context: Context
) {

    private val prefs =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit { putString("token", token) }
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }
}