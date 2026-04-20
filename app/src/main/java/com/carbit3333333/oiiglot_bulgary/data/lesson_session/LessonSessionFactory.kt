package com.carbit3333333.oiiglot_bulgary.data.lesson_session

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
        assets: LessonSessionAssets
    ): LessonSession {
        return when (lessonId) {
            1 -> LessonSession(
                lessonId = 1,
                lessonTitle = "Урок 1",
                exercises = Lesson1RealGenerator.generateExercises()
            )

            2 -> LessonSession(
                lessonId = 2,
                lessonTitle = "Глагол \"съм\"",
                exercises = Lesson2RealGenerator.generateExercises()
            )

            3 -> LessonSession(
                lessonId = 3,
                lessonTitle = "Прошедшее время",
                exercises = generateLesson3Exercises(
                    subjectRuMap = assets.lesson3SubjectRu,
                    verbs = assets.lesson3Verbs
                )
            )

            4 -> LessonSession(
                lessonId = 4,
                lessonTitle = "Предмет или действие",
                exercises = generateLesson4Exercises(assets.lesson4Items)
            )

            5 -> LessonSession(
                lessonId = 5,
                lessonTitle = "Могу, хочу, должен",
                exercises = Lesson5RealGenerator.generateExercises()
            )

            6 -> LessonSession(
                lessonId = 6,
                lessonTitle = "Предлоги и существительные",
                exercises = Lesson6RealGenerator.generateExercises()
            )

            7 -> LessonSession(
                lessonId = 7,
                lessonTitle = "Моя книга: местоимения и артикль",
                exercises = Lesson7RealGenerator.generateExercises(assets.lesson7Templates)
            )

            8 -> LessonSession(
                lessonId = 8,
                lessonTitle = "Сравнение",
                exercises = Lesson8RealGenerator.generateExercises(assets.lesson8Templates)
            )

            9 -> LessonSession(
                lessonId = 9,
                lessonTitle = "Числа",
                exercises = Lesson9RealGenerator.generateExercises(
                    numbers = assets.lesson9Numbers,
                    objects = assets.lesson9Objects,
                    templates = assets.lesson9Templates
                )
            )

            else -> LessonSession(
                lessonId = lessonId,
                lessonTitle = "Урок $lessonId",
                exercises = generateLesson1Exercises()
            )
        }
    }
}
