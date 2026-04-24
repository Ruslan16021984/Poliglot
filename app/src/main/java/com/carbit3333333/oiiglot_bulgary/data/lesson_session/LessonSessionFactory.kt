package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson10RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson1RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson2RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson5RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson6RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson7RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson8RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson9RealGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonSession

internal object LessonSessionFactory {
    fun create(
        lessonId: Int,
        assets: LessonSessionAssets,
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    ): LessonSession {
        val session = when (lessonId) {
            1 -> LessonSession(
                lessonId = 1,
                lessonTitle = "Урок 1",
                exercises = Lesson1RealGenerator.generateExercises(
                    fixedSentences = assets.lesson1Sentences,
                    subjects = assets.lesson1Subjects,
                    templates = assets.lesson1Templates,
                    verbs = assets.lesson1Verbs,
                    exerciseLocale = exerciseLocale,
                ),
            )

            2 -> LessonSession(
                lessonId = 2,
                lessonTitle = "Глагол \"съм\"",
                exercises = Lesson2RealGenerator.generateExercises(
                    exerciseLocale = exerciseLocale,
                ),
            )

            3 -> LessonSession(
                lessonId = 3,
                lessonTitle = "Прошедшее время",
                exercises = generateLesson3Exercises(
                    subjectRuMap = assets.lesson3SubjectRu,
                    subjectUkMap = assets.lesson3SubjectUk,
                    verbs = assets.lesson3Verbs,
                    exerciseLocale = exerciseLocale,
                ),
            )

            4 -> LessonSession(
                lessonId = 4,
                lessonTitle = "Предмет или действие",
                exercises = generateLesson4Exercises(
                    items = assets.lesson4Items,
                    exerciseLocale = exerciseLocale,
                ),
            )

            5 -> LessonSession(
                lessonId = 5,
                lessonTitle = "Могу, хочу, должен",
                exercises = Lesson5RealGenerator.generateExercises(
                    exerciseLocale = exerciseLocale,
                ),
            )

            6 -> LessonSession(
                lessonId = 6,
                lessonTitle = "Предлоги и существительные",
                exercises = Lesson6RealGenerator.generateExercises(
                    exerciseLocale = exerciseLocale,
                ),
            )

            7 -> LessonSession(
                lessonId = 7,
                lessonTitle = "Моя книга: местоимения и артикль",
                exercises = Lesson7RealGenerator.generateExercises(
                    templates = assets.lesson7Templates,
                    exerciseLocale = exerciseLocale,
                ),
            )

            8 -> LessonSession(
                lessonId = 8,
                lessonTitle = "Сравнение",
                exercises = Lesson8RealGenerator.generateExercises(
                    templates = assets.lesson8Templates,
                    exerciseLocale = exerciseLocale,
                ),
            )

            9 -> LessonSession(
                lessonId = 9,
                lessonTitle = "Числа",
                exercises = Lesson9RealGenerator.generateExercises(
                    numbers = assets.lesson9Numbers,
                    objects = assets.lesson9Objects,
                    templates = assets.lesson9Templates,
                    exerciseLocale = exerciseLocale,
                ),
            )

            10 -> LessonSession(
                lessonId = 10,
                lessonTitle = "Время и распорядок дня",
                exercises = Lesson10RealGenerator.generateExercises(
                    timePhrases = assets.lesson10TimePhrases,
                    routineActions = assets.lesson10RoutineActions,
                    intervals = assets.lesson10Intervals,
                    intervalActions = assets.lesson10IntervalActions,
                    questionActions = assets.lesson10QuestionActions,
                    templates = assets.lesson10Templates,
                    intervalTemplates = assets.lesson10IntervalTemplates,
                    questionTemplates = assets.lesson10QuestionTemplates,
                    exerciseLocale = exerciseLocale,
                ),
            )

            else -> LessonSession(
                lessonId = lessonId,
                lessonTitle = "Урок $lessonId",
                exercises = generateLesson1Exercises(),
            )
        }

        return if (exerciseLocale == LessonExerciseLocale.Ukrainian) {
            session.copy(
                lessonTitle = UkrainianLessonStrings.lessonTitle(session.lessonId),
                exercises = if (session.lessonId in setOf(2, 4, 5, 6, 7, 8, 9, 10)) {
                    localizeLessonExercisesUsingExistingSource(session.exercises, exerciseLocale)
                } else {
                    localizeLessonExercises(session.exercises, exerciseLocale)
                },
            )
        } else {
            session
        }
    }
}
