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
    fun `all textbook lessons build a valid 100 exercise session`() {
        (1..11).forEach { lessonId ->
            val session = repository.getLessonSession(lessonId)

            assertEquals("lesson $lessonId size", 100, session.exercises.size)
            assertTrue("lesson $lessonId title", session.lessonTitle.isNotBlank())
            assertTrue("lesson $lessonId valid exercises", session.exercises.all(::isValidExercise))
        }
    }

    @Test
    fun `expanded textbook lessons keep a broad phrase pool`() {
        (1..11).forEach { lessonId ->
            val session = repository.getLessonSession(lessonId)
            assertTrue("lesson $lessonId unique prompts", session.exercises.map { it.sourceText }.toSet().size >= 50)
        }
    }

    private fun isValidExercise(exercise: LessonExercise): Boolean {
        assertTrue(exercise.sourceText.isNotBlank())
        assertTrue(exercise.instruction.isNotBlank())
        assertFalse(exercise.correctAnswerWords.isEmpty())
        assertEquals(8, exercise.availableWords.size)
        assertTrue(exercise.correctAnswerWords.distinct().all { it in exercise.availableWords })
        return true
    }
}
