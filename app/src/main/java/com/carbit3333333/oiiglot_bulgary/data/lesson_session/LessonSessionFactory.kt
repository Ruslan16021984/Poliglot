package com.carbit3333333.oiiglot_bulgary.data.lesson_session

import com.carbit3333333.oiiglot_bulgary.model.LessonSession

internal object LessonSessionFactory {
    fun create(lessonId: Int): LessonSession {
        return when (lessonId) {
            1 -> LessonSession(
                lessonId = 1,
                lessonTitle = "Урок 1",
                exercises = generateLesson1Exercises()
            )

            2 -> LessonSession(
                lessonId = 2,
                lessonTitle = "Глагол \"съм\"",
                exercises = generateLesson2Exercises()
            )

            3 -> LessonSession(
                lessonId = 3,
                lessonTitle = "Прошедшее время",
                exercises = generateLesson3Exercises()
            )

            4 -> LessonSession(
                lessonId = 4,
                lessonTitle = "Предмет или действие",
                exercises = generateLesson4Exercises()
            )

            5 -> LessonSession(
                lessonId = 5,
                lessonTitle = "Могу, хочу, должен",
                exercises = generateLesson5Exercises()
            )

            6 -> LessonSession(
                lessonId = 6,
                lessonTitle = "Предлоги и существительные",
                exercises = generateLesson6Exercises()
            )

            7 -> LessonSession(
                lessonId = 7,
                lessonTitle = "Моя книга: местоимения и артикль",
                exercises = generateLesson7Exercises()
            )

            8 -> LessonSession(
                lessonId = 8,
                lessonTitle = "Сравнение",
                exercises = generateLesson8Exercises()
            )

            else -> LessonSession(
                lessonId = lessonId,
                lessonTitle = "Урок $lessonId",
                exercises = generateLesson1Exercises()
            )
        }
    }
}
