package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.data.localization.resolveLocalizedValue
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal data class TextbookLessonExerciseItemAsset(
    val id: String,
    val theme: String,
    val grammarFocus: List<String> = emptyList(),
    val difficulty: String = "easy",
    val bg: String,
    val ru: String? = null,
    val uk: String? = null,
    val source: Map<String, String> = emptyMap(),
    val correctWords: List<String>,
    val distractors: List<String> = emptyList(),
    val hintRu: String? = null,
    val hintUk: String? = null,
    val hint: Map<String, String> = emptyMap(),
) {
    fun resolveSourceText(
        languageCode: String,
        fallbackLanguageCode: String,
    ): String {
        val localizedText = resolveLocalizedValue(
            valuesByLanguage = source + legacySourceTranslations(),
            requestedLanguageCode = languageCode,
            fallbackLanguageCode = fallbackLanguageCode,
        )

        if (localizedText != null) {
            return localizedText
        }

        if (languageCode == "en" || languageCode.startsWith("en-")) {
            ru?.let(::translateRussianExerciseTextToEnglish)?.let { return it }
        }

        return ru ?: uk ?: bg
    }

    fun resolveHint(
        languageCode: String,
        fallbackLanguageCode: String,
    ): String? {
        val localizedHint = resolveLocalizedValue(
            valuesByLanguage = hint + legacyHintTranslations(),
            requestedLanguageCode = languageCode,
            fallbackLanguageCode = fallbackLanguageCode,
        )

        if (localizedHint != null) {
            return localizedHint
        }

        if (languageCode == "en" || languageCode.startsWith("en-")) {
            return null
        }

        return hintRu ?: hintUk
    }

    private fun legacySourceTranslations(): Map<String, String> {
        return buildMap {
            ru?.let { put("ru", it) }
            uk?.let { put("uk", it) }
        }
    }

    private fun legacyHintTranslations(): Map<String, String> {
        return buildMap {
            hintRu?.let { put("ru", it) }
            hintUk?.let { put("uk", it) }
        }
    }
}

@Serializable
internal data class TextbookLessonExerciseSetAsset(
    val lessonApp: Int,
    val lessonBook: Int,
    val title: String,
    val titleTranslations: Map<String, String> = emptyMap(),
    val exerciseType: String,
    val source: String,
    val items: List<TextbookLessonExerciseItemAsset>,
) {
    fun resolveTitle(
        languageCode: String,
        fallbackLanguageCode: String,
        lessonId: Int,
    ): String {
        val localizedTitle = resolveLocalizedValue(
            valuesByLanguage = titleTranslations,
            requestedLanguageCode = languageCode,
            fallbackLanguageCode = fallbackLanguageCode,
        )

        return LessonExerciseStrings.lessonTitle(
            languageCode = languageCode,
            lessonId = lessonId,
            defaultTitle = localizedTitle ?: title,
        )
    }
}

internal class TextbookLessonExercisesRepository private constructor(
    private val assetReader: (String) -> String?,
) {

    constructor(context: Context) : this(
        assetReader = { fileName ->
            try {
                context.assets.open(fileName).bufferedReader(Charsets.UTF_8).use { it.readText() }
            } catch (_: Exception) {
                null
            }
        },
    )

    constructor() : this(
        assetReader = { fileName ->
            resolveLocalAssetPath(fileName)?.let {
                String(Files.readAllBytes(it), StandardCharsets.UTF_8)
            }
        },
    )

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun loadForLesson(lessonId: Int): TextbookLessonExerciseSetAsset? {
        val fileName = "textbook_exercises_lesson$lessonId.json"
        val text = assetReader(fileName) ?: return null
        return runCatching {
            json.decodeFromString<TextbookLessonExerciseSetAsset>(text)
        }.getOrNull()
    }

    private companion object {
        fun resolveLocalAssetPath(fileName: String): Path? {
            val directPath = Paths.get("src", "main", "assets", fileName)
            if (Files.exists(directPath)) {
                return directPath
            }

            val appPath = Paths.get("app", "src", "main", "assets", fileName)
            if (Files.exists(appPath)) {
                return appPath
            }

            return null
        }
    }
}
