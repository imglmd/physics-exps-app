package com.imglmd.physicsexps.core.ui.utils
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.compose.koinInject

@Composable
fun getStringByKey(
    key: String?,
    providers: List<StringKeyProvider> = koinInject()
): String {
    if (key.isNullOrBlank()) return ""
    val resId = providers.firstNotNullOfOrNull { it.resolve(key) } ?: return ""
    return stringResource(resId)
}