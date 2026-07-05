package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.data.localization.DEFAULT_CONTENT_LANGUAGE_CODE
import com.carbit3333333.oiiglot_bulgary.data.localization.resolveCurrentLanguageCode
import com.carbit3333333.oiiglot_bulgary.model.TheoryBlock
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import com.carbit3333333.oiiglot_bulgary.model.theory.StructuredTheoryLessonAsset
import com.carbit3333333.oiiglot_bulgary.model.theory.toStructuredTheoryBlocks
import kotlinx.serialization.json.Json

class LessonRepository(
    context: Context,
) {

    private val appContext = context.applicationContext

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private var cachedAssetFile: String? = null
    private var lessonsCache: List<Lesson> = emptyList()

    @Synchronized
    fun getLessons(): List<Lesson> {
        val localizedFile = localizedLessonsAssetFile()
        if (localizedFile != cachedAssetFile) {
            lessonsCache = loadLessonsFromAssets(localizedFile)
            cachedAssetFile = localizedFile
        }
        return lessonsCache
    }

    fun getLessonById(lessonId: Int): Lesson? {
        return getLessons().find { it.id == lessonId }
    }

    fun hasNextLesson(currentLessonId: Int): Boolean {
        return getLessons().any { it.id == currentLessonId + 1 }
    }

    fun getNextLessonId(currentLessonId: Int): Int? {
        return getLessons().find { it.id == currentLessonId + 1 }?.id
    }

    private fun loadLessonsFromAssets(localizedFile: String): List<Lesson> {
        return runCatching {
            val lessons = decodeAsset<List<Lesson>>(localizedFile)
            val structuredTheoryByLessonId = loadStructuredTheory(localizedTheoryAssetFile())

            lessons.map { lesson ->
                val structuredTheory = structuredTheoryByLessonId[lesson.id]?.blocks
                    ?: lesson.theory.toStructuredTheoryBlocks()
                localizeLessonMetadata(
                    lesson = lesson.copy(structuredTheory = structuredTheory),
                    languageCode = resolveCurrentLanguageCode(appContext.resources),
                )
            }
        }.getOrDefault(emptyList())
    }

    private fun loadStructuredTheory(localizedFile: String): Map<Int, StructuredTheoryLessonAsset> {
        val theoryAssets = runCatching {
            decodeAsset<List<StructuredTheoryLessonAsset>>(localizedFile)
        }.getOrDefault(emptyList())

        return theoryAssets.associateBy { it.lessonId }
    }

    private inline fun <reified T> decodeAsset(fileName: String): T {
        return appContext.assets
            .open(fileName)
            .bufferedReader(Charsets.UTF_8)
            .use { reader ->
                json.decodeFromString<T>(reader.readText())
            }
    }

    private fun localizedLessonsAssetFile(): String {
        return resolveLocalizedAssetFile(
            mapOf(
                "ru" to LESSONS_RU_ASSET_FILE,
                "uk" to LESSONS_UK_ASSET_FILE,
            )
        )
    }

    private fun localizedTheoryAssetFile(): String {
        return resolveLocalizedAssetFile(
            mapOf(
                "ru" to THEORY_RU_ASSET_FILE,
                "uk" to THEORY_UK_ASSET_FILE,
            )
        )
    }

    private fun resolveLocalizedAssetFile(assetFilesByLanguage: Map<String, String>): String {
        val requestedLanguageCode = resolveCurrentLanguageCode(appContext.resources)
        return assetFilesByLanguage[requestedLanguageCode]
            ?: assetFilesByLanguage[requestedLanguageCode.substringBefore('-')]
            ?: assetFilesByLanguage[DEFAULT_CONTENT_LANGUAGE_CODE]
            ?: assetFilesByLanguage.values.first()
    }

    private fun localizeLessonMetadata(
        lesson: Lesson,
        languageCode: String,
    ): Lesson {
        val localizedMetadata = ENGLISH_LESSON_METADATA[lesson.id]
            ?.takeIf { languageCode == "en" || languageCode.startsWith("en-") }
            ?: return lesson

        return lesson.copy(
            title = localizedMetadata.title,
            subtitle = localizedMetadata.subtitle,
        )
    }

    private companion object {
        const val LESSONS_RU_ASSET_FILE = "lessons_ru.json"
        const val LESSONS_UK_ASSET_FILE = "lessons_uk.json"
        const val THEORY_RU_ASSET_FILE = "lesson_theory_ru.json"
        const val THEORY_UK_ASSET_FILE = "lesson_theory_uk.json"

        val ENGLISH_LESSON_METADATA = mapOf(
            1 to LessonMetadata(title = "Lesson 1", subtitle = "Greetings and Introductions"),
            2 to LessonMetadata(title = "Lesson 2", subtitle = "Food and Breakfast"),
            3 to LessonMetadata(title = "Lesson 3", subtitle = "Restaurant"),
            4 to LessonMetadata(title = "Lesson 4", subtitle = "Shopping at the Supermarket and Market"),
            5 to LessonMetadata(title = "Lesson 5", subtitle = "City, Address and Directions"),
            6 to LessonMetadata(title = "Lesson 6", subtitle = "Family"),
            7 to LessonMetadata(title = "Lesson 7", subtitle = "Weather and Time"),
            8 to LessonMetadata(title = "Lesson 8", subtitle = "Clothes and Colors"),
            9 to LessonMetadata(title = "Lesson 9", subtitle = "Home and Furniture"),
            10 to LessonMetadata(title = "Lesson 10", subtitle = "Transport"),
            11 to LessonMetadata(title = "Lesson 11", subtitle = "Daily Routine"),
        )
    }
}

private data class LessonMetadata(
    val title: String,
    val subtitle: String,
)
