package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generateLesson4Exercise
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.localizeLessonExercises
import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonExerciseLocalizationTest {

    @Test
    fun `ukrainian localization translates exercise source instruction and hint`() {
        val exercise = LessonExercise(
            id = 1,
            sourceText = "Мы любим кофе дома?",
            instruction = "Переведите предложение",
            correctAnswerWords = listOf("Ние", "обичаме", "ли", "кафе", "вкъщи"),
            availableWords = listOf("Ние", "обичаме", "ли", "кафе", "вкъщи", "Аз", "не", "да"),
            hint = "Вопрос: глагол + ли",
        )

        val localized = localizeLessonExercises(
            exercises = listOf(exercise),
            locale = LessonExerciseLocale.Ukrainian,
        ).single()

        assertEquals("Перекладіть речення", localized.instruction)
        assertEquals("Ми любимо каву вдома?", localized.sourceText)
        assertTrue(localized.hint?.contains("Питання") == true)
    }

    @Test
    fun `lesson 4 uses exact ukrainian source text when it exists`() {
        val exercise = generateLesson4Exercise(
            id = 1,
            items = listOf(
                Lesson4Item(
                    type = Lesson4Item.Type.NOUN,
                    ru = "я хочу эту книгу",
                    uk = "я хочу цю книгу",
                    correctWords = listOf("Аз", "искам", "книгата"),
                ),
                Lesson4Item(
                    type = Lesson4Item.Type.VERB,
                    ru = "мы любим читать",
                    uk = "ми любимо читати",
                    correctWords = listOf("Ние", "обичаме", "да", "чета"),
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        )

        assertEquals("я хочу цю книгу", exercise.sourceText)
    }
}
