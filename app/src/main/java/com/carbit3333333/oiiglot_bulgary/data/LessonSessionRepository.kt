package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssetsRepository
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionFactory
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.defaultLessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.model.LessonSession
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.json.Json

class LessonSessionRepository private constructor(
    private val sessionAssets: LessonSessionAssets
) {

    constructor(context: Context) : this(
        sessionAssets = LessonSessionAssetsRepository(context).load()
    )

    constructor() : this(
        sessionAssets = loadLocalAssetsOrDefault()
    )

    fun getLessonSession(lessonId: Int): LessonSession {
        return LessonSessionFactory.create(
            lessonId = lessonId,
            assets = sessionAssets
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
}
