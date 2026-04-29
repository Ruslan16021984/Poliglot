package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.generateTextbookLessonExercises
import com.carbit3333333.oiiglot_bulgary.model.LessonSession

internal object LessonSessionFactory {
    fun create(
        lessonId: Int,
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
        textbookExercises: TextbookLessonExerciseSetAsset,
    ): LessonSession {
        val session = LessonSession(
            lessonId = lessonId,
            lessonTitle = textbookExercises.title,
            exercises = generateTextbookLessonExercises(
                exerciseSet = textbookExercises,
                exerciseLocale = exerciseLocale,
            ),
        )

        return if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            session.copy(
                lessonTitle = UkrainianLessonStrings.lessonTitle(session.lessonId),
                exercises = session.exercises,
            )
        } else {
            session
        }
    }
}
