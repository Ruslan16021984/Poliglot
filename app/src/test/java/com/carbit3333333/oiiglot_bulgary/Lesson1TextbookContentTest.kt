package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Lesson1TextbookContentTest {

    private val repository = LessonSessionRepository()

    @Test
    fun `lesson 1 should use textbook introduction phrases`() {
        val session = repository.getLessonSession(1)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertTrue(sourceTexts.any { it.contains("Здравствуйте") })
        assertTrue(sourceTexts.any { it.contains("Очень приятно") })
        assertTrue(sourceTexts.any { it.contains("Откуда") })
        assertTrue(sourceTexts.any { it.contains("Я из Болгарии") || it.contains("Я из Сирии") })
        assertTrue(sourceTexts.any { it.contains("беженец") || it.contains("учитель") })
    }

    @Test
    fun `lesson 1 should not contain legacy grammar driven prompts`() {
        val session = repository.getLessonSession(1)
        val sourceTexts = session.exercises.map { it.sourceText.lowercase() }

        assertFalse(sourceTexts.any { it.contains("буду") })
        assertFalse(sourceTexts.any { it.contains("не будет") })
        assertFalse(sourceTexts.any { it.contains("учусь дома") })
        assertFalse(sourceTexts.any { it.contains("говорю медленно") })
    }
}
