package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import java.util.Locale
import kotlinx.serialization.json.Json

class LessonRepository(
    context: Context,
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
                return applyTextbookDisplayOverrides(lessons)
            }
        }

        return emptyList()
    }

    private fun applyTextbookDisplayOverrides(lessons: List<Lesson>): List<Lesson> {
        val language = appContext.resources.configuration.locales[0]
            ?.language
            ?.lowercase(Locale.ROOT)

        val subtitleOverrides = when (language) {
            "uk" -> mapOf(
                1 to "Привітання і знайомство",
                2 to "Їжа і сніданок",
                3 to "Ресторан",
                4 to "Покупки в супермаркеті і на ринку",
                5 to "Місто, адреса і покупки",
                6 to "Сім'я",
                7 to "Погода і час",
                8 to "Одяг і кольори",
                9 to "Дім і меблі",
                10 to "Транспорт",
            )

            else -> mapOf(
                1 to "Приветствие и знакомство",
                2 to "Еда и завтрак",
                3 to "Ресторан",
                4 to "Покупки в супермаркете и на рынке",
                5 to "Город, адрес и покупки",
                6 to "Семья",
                7 to "Погода и время",
                8 to "Одежда и цвета",
                9 to "Дом и мебель",
                10 to "Транспорт",
            )
        }

        return lessons.map { lesson ->
            val subtitle = subtitleOverrides[lesson.id] ?: return@map lesson
            lesson.copy(subtitle = subtitle)
        }
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
