package com.carbit3333333.oiiglot_bulgary.data.localization

import android.content.res.Resources
import com.carbit3333333.oiiglot_bulgary.data.settings.AppLanguage
import java.util.Locale

internal const val DEFAULT_CONTENT_LANGUAGE_CODE = "ru"

internal fun normalizeLanguageCode(rawLanguageCode: String?): String? {
    return rawLanguageCode
        ?.trim()
        ?.lowercase(Locale.ROOT)
        ?.takeIf(String::isNotBlank)
}

internal fun resolveCurrentLanguageCode(resources: Resources): String {
    val currentLanguageCode = runCatching {
        resources.configuration.locales[0]?.language
    }.getOrNull()

    return normalizeLanguageCode(currentLanguageCode) ?: DEFAULT_CONTENT_LANGUAGE_CODE
}

internal fun resolveRequestedLanguageCode(
    resources: Resources,
    appLanguage: AppLanguage? = null,
): String {
    return appLanguage?.tag
        ?.takeIf(String::isNotBlank)
        ?.let(::normalizeLanguageCode)
        ?: resolveCurrentLanguageCode(resources)
}

internal fun resolveLocalizedValue(
    valuesByLanguage: Map<String, String>,
    requestedLanguageCode: String,
    fallbackLanguageCode: String = DEFAULT_CONTENT_LANGUAGE_CODE,
): String? {
    val normalizedRequested = normalizeLanguageCode(requestedLanguageCode) ?: fallbackLanguageCode
    val normalizedFallback = normalizeLanguageCode(fallbackLanguageCode) ?: DEFAULT_CONTENT_LANGUAGE_CODE

    return valuesByLanguage[normalizedRequested]
        ?: valuesByLanguage[normalizedRequested.substringBefore('-')]
        ?: valuesByLanguage[normalizedFallback]
        ?: valuesByLanguage[normalizedFallback.substringBefore('-')]
}
