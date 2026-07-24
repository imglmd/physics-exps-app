package com.imglmd.physicsexps.core.ui.utils

import androidx.annotation.StringRes

/**
 *
 */
fun interface StringKeyProvider {
    @StringRes
    fun resolve(key: String): Int?
}