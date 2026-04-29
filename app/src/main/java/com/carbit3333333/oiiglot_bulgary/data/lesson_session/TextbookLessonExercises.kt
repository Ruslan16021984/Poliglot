package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import android.content.Context
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
    val ru: String,
    val uk: String,
    val correctWords: List<String>,
    val distractors: List<String> = emptyList(),
    val hintRu: String? = null,
    val hintUk: String? = null,
)

@Serializable
internal data class TextbookLessonExerciseSetAsset(
    val lessonApp: Int,
    val lessonBook: Int,
    val title: String,
    val exerciseType: String,
    val source: String,
    val items: List<TextbookLessonExerciseItemAsset>,
)

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
