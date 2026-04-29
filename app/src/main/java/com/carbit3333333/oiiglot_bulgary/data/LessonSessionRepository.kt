package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssetsRepository
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionFactory
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.TextbookLessonExerciseSetAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.TextbookLessonExercisesRepository
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.defaultLessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.resolveLessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.model.LessonSession
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.json.Json

class LessonSessionRepository private constructor(
    private val sessionAssets: LessonSessionAssets,
    private val exerciseLocale: LessonExerciseLocale,
    private val textbookExercisesRepository: TextbookLessonExercisesRepository,
) {

    constructor(context: Context) : this(
        sessionAssets = LessonSessionAssetsRepository(context).load(),
        exerciseLocale = resolveLessonExerciseLocale(context),
        textbookExercisesRepository = TextbookLessonExercisesRepository(context),
    )

    constructor() : this(
        sessionAssets = loadLocalAssetsOrDefault(),
        exerciseLocale = LessonExerciseLocale.Russian,
        textbookExercisesRepository = TextbookLessonExercisesRepository(),
    )

    fun getLessonSession(lessonId: Int): LessonSession {
        return LessonSessionFactory.create(
            lessonId = lessonId,
            assets = sessionAssets,
            exerciseLocale = exerciseLocale,
            textbookExercises = loadTextbookExercisesForLesson(lessonId),
        )
    }

    private companion object {
        fun loadLocalAssetsOrDefault(): LessonSessionAssets {
            val json = Json {
                ignoreUnknownKeys = true
            }

            val localPath = resolveLocalAssetPath("lesson_session_content.json")
            if (localPath != null) {
                return runCatching {
                    json.decodeFromString<LessonSessionAssets>(
                        String(Files.readAllBytes(localPath), StandardCharsets.UTF_8)
                    )
                }.getOrElse {
                    defaultLessonSessionAssets()
                }
            }

            return defaultLessonSessionAssets()
        }

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

    private fun loadTextbookExercisesForLesson(lessonId: Int): TextbookLessonExerciseSetAsset? {
        val alignedTextbookLessons = setOf(1, 4)
        if (lessonId !in alignedTextbookLessons) return null
        return textbookExercisesRepository.loadForLesson(lessonId)
    }
}
