package com.imglmd.core.ui.utils
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

@Composable
fun getStringByKey(
    key: String?,
    providers: List<StringKeyProvider> = koinInject(qualifier = named("stringProviders"))
): String {
    if (key.isNullOrBlank()) return ""
    val resId = providers.firstNotNullOfOrNull { it.resolve(key) } ?: return ""
    return stringResource(resId)
}