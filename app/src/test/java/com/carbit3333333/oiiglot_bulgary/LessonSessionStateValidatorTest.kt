package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.isRestorableLessonSessionState
import com.carbit3333333.oiiglot_bulgary.data.relocalizeRestoredLessonSessionState
import com.carbit3333333.oiiglot_bulgary.model.ExerciseResult
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import com.carbit3333333.oiiglot_bulgary.model.LessonResult
import com.carbit3333333.oiiglot_bulgary.model.LessonSession
import com.carbit3333333.oiiglot_bulgary.ui.lessons.LessonSessionUiState
import org.junit.Assert.assertEquals
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

    @Test
    fun `validator rejects finished lesson session`() {
        assertFalse(
            isRestorableLessonSessionState(
                validState().copy(isLessonFinished = true)
            )
        )
    }

    @Test
    fun `validator rejects lesson session with result payload`() {
        assertFalse(
            isRestorableLessonSessionState(
                validState().copy(
                    lessonResult = LessonResult(
                        lessonId = 1,
                        lessonTitle = "Урок 1",
                        totalExercises = 1,
                        correctCount = 1,
                        wrongCount = 0,
                        score = 5f,
                        isPassed = true
                    )
                )
            )
        )
    }

    @Test
    fun `restored session uses current localized texts without losing progress`() {
        val savedState = validState().copy(
            currentExerciseIndex = 0,
            correctCount = 4,
            wrongCount = 1,
            results = listOf(ExerciseResult.CORRECT),
        )
        val localizedSession = LessonSession(
            lessonId = 1,
            lessonTitle = "Урок 1 українською",
            exercises = listOf(
                lessonExercise().copy(
                    sourceText = "Я Хасан.",
                    instruction = "Перекладіть речення",
                    hint = "Представлення",
                ),
            ),
        )

        val restored = relocalizeRestoredLessonSessionState(savedState, localizedSession)

        assertEquals("Урок 1 українською", restored.lessonTitle)
        assertEquals("Перекладіть речення", restored.exercises.single().instruction)
        assertEquals("Представлення", restored.exercises.single().hint)
        assertEquals(savedState.correctCount, restored.correctCount)
        assertEquals(savedState.wrongCount, restored.wrongCount)
        assertEquals(savedState.results, restored.results)
        assertEquals(savedState.exercises.single().correctAnswerWords, restored.exercises.single().correctAnswerWords)
        assertEquals(savedState.exercises.single().availableWords, restored.exercises.single().availableWords)
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
