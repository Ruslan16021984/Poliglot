package com.carbit3333333.oiiglot_bulgary.data

import android.content.Context
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionFactory
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.TextbookLessonExerciseSetAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.TextbookLessonExercisesRepository
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.resolveLessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.model.LessonSession

class LessonSessionRepository private constructor(
    private val exerciseLocale: LessonExerciseLocale,
    private val textbookExercisesRepository: TextbookLessonExercisesRepository,
) {

    constructor(context: Context) : this(
        exerciseLocale = resolveLessonExerciseLocale(context),
        textbookExercisesRepository = TextbookLessonExercisesRepository(context),
    )

    constructor() : this(
        exerciseLocale = LessonExerciseLocale.Russian,
        textbookExercisesRepository = TextbookLessonExercisesRepository(),
    )

    fun getLessonSession(lessonId: Int): LessonSession {
        val textbookExercises = requireNotNull(loadTextbookExercisesForLesson(lessonId)) {
            "Textbook exercises not found for lesson $lessonId"
        }

        return LessonSessionFactory.create(
            lessonId = lessonId,
            exerciseLocale = exerciseLocale,
            textbookExercises = textbookExercises,
        )
    }

    private fun loadTextbookExercisesForLesson(lessonId: Int): TextbookLessonExerciseSetAsset? {
        val alignedTextbookLessons = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        if (lessonId !in alignedTextbookLessons) return null
        return textbookExercisesRepository.loadForLesson(lessonId)
    }
}
