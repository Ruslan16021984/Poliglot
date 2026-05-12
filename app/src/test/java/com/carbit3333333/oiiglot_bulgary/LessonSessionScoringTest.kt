package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.viewmodel.PASSING_SCORE
import com.carbit3333333.oiiglot_bulgary.viewmodel.calculateLessonScore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonSessionScoringTest {

    @Test
    fun `score is based on full lesson length`() {
        assertEquals(0.5f, calculateLessonScore(correctCount = 10, totalExercises = 100), 0.0001f)
        assertEquals(4.5f, calculateLessonScore(correctCount = 90, totalExercises = 100), 0.0001f)
        assertEquals(5.0f, calculateLessonScore(correctCount = 100, totalExercises = 100), 0.0001f)
    }

    @Test
    fun `ninety correct answers are enough to pass before finishing all exercises`() {
        val score = calculateLessonScore(correctCount = 90, totalExercises = 100)
        assertTrue(score >= PASSING_SCORE)
    }
}
