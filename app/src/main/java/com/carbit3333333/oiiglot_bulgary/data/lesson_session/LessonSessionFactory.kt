package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.generateTextbookLessonExercises
import com.carbit3333333.oiiglot_bulgary.model.LessonSession

internal object LessonSessionFactory {
    fun create(
        lessonId: Int,
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale(languageCode = "ru"),
        textbookExercises: TextbookLessonExerciseSetAsset,
    ): LessonSession {
        return LessonSession(
            lessonId = lessonId,
            lessonTitle = textbookExercises.resolveTitle(
                languageCode = exerciseLocale.languageCode,
                fallbackLanguageCode = exerciseLocale.fallbackLanguageCode,
                lessonId = lessonId,
            ),
            exercises = generateTextbookLessonExercises(
                exerciseSet = textbookExercises,
                exerciseLocale = exerciseLocale,
            ),
        )
    }
}
