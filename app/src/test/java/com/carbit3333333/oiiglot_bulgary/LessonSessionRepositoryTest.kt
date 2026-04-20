package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonSessionRepositoryTest {

    private val repository = LessonSessionRepository()

    @Test
    fun `lesson 1 session builds complete exercises`() {
        val session = repository.getLessonSession(1)

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
    }

    @Test
    fun `lesson 3 session uses migrated json data`() {
        val session = repository.getLessonSession(3)

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(session.exercises.any { "не" in it.correctAnswerWords })
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("(а)") ||
                    it.sourceText.contains(" / шла") ||
                    it.sourceText.contains("(лась)")
            }
        )
    }

    @Test
    fun `lesson 7 and 8 sessions use migrated template content`() {
        val lesson7 = repository.getLessonSession(7)
        val lesson8 = repository.getLessonSession(8)

        assertEquals(60, lesson7.exercises.size)
        assertEquals(60, lesson8.exercises.size)
        assertTrue(lesson7.exercises.all(::isValidExercise))
        assertTrue(lesson8.exercises.all(::isValidExercise))
        assertTrue(lesson7.exercises.any { it.sourceText.contains("своего друга") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("лучший день") })
    }

    private fun isValidExercise(exercise: LessonExercise): Boolean {
        assertTrue(exercise.sourceText.isNotBlank())
        assertEquals("Переведите предложение", exercise.instruction)
        assertFalse(exercise.correctAnswerWords.isEmpty())
        assertEquals(8, exercise.availableWords.size)
        assertTrue(exercise.correctAnswerWords.distinct().all { it in exercise.availableWords })
        return true
    }
}
