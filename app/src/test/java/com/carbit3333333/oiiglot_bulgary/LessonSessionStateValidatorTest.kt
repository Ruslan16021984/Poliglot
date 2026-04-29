package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.isRestorableLessonSessionState
import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonSessionUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonSessionStateValidatorTest {

    @Test
    fun `validator accepts a normal saved lesson session`() {
        assertTrue(isRestorableLessonSessionState(validState()))
    }

    @Test
    fun `validator rejects empty exercises`() {
        assertFalse(
            isRestorableLessonSessionState(
                validState().copy(
                    exercises = emptyList(),
                    results = emptyList()
                )
            )
        )
    }

    @Test
    fun `validator rejects out of bounds exercise index`() {
        assertFalse(
            isRestorableLessonSessionState(
                validState().copy(currentExerciseIndex = 3)
            )
        )
    }

    @Test
    fun `validator rejects malformed exercise words`() {
        val malformedExercise = lessonExercise(
            correctAnswerWords = listOf("Аз", "съм"),
            availableWords = listOf("Аз", "съм", "Ти")
        )

        assertFalse(
            isRestorableLessonSessionState(
                validState().copy(exercises = listOf(malformedExercise), results = listOf(ExerciseResult.NONE))
            )
        )
    }

    private fun validState(): LessonSessionUiState {
        return LessonSessionUiState(
            lessonTitle = "Урок 1",
            exercises = listOf(lessonExercise()),
            currentExerciseIndex = 0,
            results = listOf(ExerciseResult.NONE)
        )
    }

    private fun lessonExercise(
        correctAnswerWords: List<String> = listOf("Аз", "съм", "Хасан"),
        availableWords: List<String> = listOf("Аз", "съм", "Хасан", "Ти", "е", "тя", "от", "Сирия")
    ): LessonExercise {
        return LessonExercise(
            id = 1,
            sourceText = "Я Хасан.",
            instruction = "Переведите предложение",
            correctAnswerWords = correctAnswerWords,
            availableWords = availableWords,
            hint = "Представление"
        )
    }
}
