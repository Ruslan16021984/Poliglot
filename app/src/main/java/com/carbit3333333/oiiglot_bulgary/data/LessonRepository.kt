package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
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
                lesson.copy(structuredTheory = structuredTheory)
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
        val language = appContext.resources.configuration.locales[0]
            ?.language
            ?.lowercase()

        return when (language) {
            "uk" -> LESSONS_UK_ASSET_FILE
            else -> LESSONS_RU_ASSET_FILE
        }
    }

    private fun localizedTheoryAssetFile(): String {
        val language = appContext.resources.configuration.locales[0]
            ?.language
            ?.lowercase()

        return when (language) {
            "uk" -> THEORY_UK_ASSET_FILE
            else -> THEORY_RU_ASSET_FILE
        }
    }

    private companion object {
        const val LESSONS_RU_ASSET_FILE = "lessons_ru.json"
        const val LESSONS_UK_ASSET_FILE = "lessons_uk.json"
        const val THEORY_RU_ASSET_FILE = "lesson_theory_ru.json"
        const val THEORY_UK_ASSET_FILE = "lesson_theory_uk.json"
    }
}
