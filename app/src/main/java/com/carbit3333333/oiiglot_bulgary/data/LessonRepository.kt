package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import kotlinx.serialization.json.Json
import java.util.Locale

class LessonRepository(
    context: Context
) {

    private val appContext = context.applicationContext

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val lessonsCache: List<Lesson> by lazy {
        loadLessonsFromAssets()
    }

    fun getLessons(): List<Lesson> = lessonsCache

    fun getLessonById(lessonId: Int): Lesson? {
        return lessonsCache.find { it.id == lessonId }
    }

    fun hasNextLesson(currentLessonId: Int): Boolean {
        return lessonsCache.any { it.id == currentLessonId + 1 }
    }

    fun getNextLessonId(currentLessonId: Int): Int? {
        return lessonsCache.find { it.id == currentLessonId + 1 }?.id
    }

    private fun loadLessonsFromAssets(): List<Lesson> {
        val localizedFile = localizedLessonsAssetFile()
        val fallbackFiles = listOf(localizedFile, LESSONS_RU_ASSET_FILE, LESSONS_LEGACY_ASSET_FILE).distinct()

        fallbackFiles.forEach { assetFile ->
            val lessons = runCatching {
                appContext.assets
                    .open(assetFile)
                    .bufferedReader(Charsets.UTF_8)
                    .use { reader ->
                        json.decodeFromString<List<Lesson>>(reader.readText())
                    }
            }.getOrNull()

            if (!lessons.isNullOrEmpty()) {
                return lessons
            }
        }

        return emptyList()
    }

    private fun localizedLessonsAssetFile(): String {
        val language = appContext.resources.configuration.locales[0]
            ?.language
            ?.lowercase(Locale.ROOT)

        return when (language) {
            "uk" -> LESSONS_UK_ASSET_FILE
            else -> LESSONS_RU_ASSET_FILE
        }
    }

    private companion object {
        const val LESSONS_LEGACY_ASSET_FILE = "lessons.json"
        const val LESSONS_RU_ASSET_FILE = "lessons_ru.json"
        const val LESSONS_UK_ASSET_FILE = "lessons_uk.json"
    }
}
