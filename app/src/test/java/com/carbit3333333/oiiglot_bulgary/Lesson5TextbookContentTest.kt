package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Lesson5TextbookContentTest {

    private val repository = LessonSessionRepository()

    @Test
    fun `lesson 5 should focus on city address prepositions and directions`() {
        val session = repository.getLessonSession(5)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertTrue(sourceTexts.any { it.contains("Музей рядом с театром.") })
        assertTrue(sourceTexts.any { it.contains("Парк перед отелем.") })
        assertTrue(sourceTexts.any { it.contains("Библиотека за банком.") })
        assertTrue(sourceTexts.any { it.contains("Почта между музеем и театром.") })
        assertTrue(sourceTexts.any { it.contains("Больница напротив парка.") })
        assertTrue(sourceTexts.any { it.contains("Кино рядом с отелем.") })
        assertTrue(sourceTexts.any { it.contains("Отель далеко от центра.") })
        assertTrue(sourceTexts.any { it.contains("Идите прямо.") })
        assertTrue(sourceTexts.any { it.contains("Поверните налево.") })
        assertTrue(sourceTexts.any { it.contains("Поверните направо.") })
    }

    @Test
    fun `lesson 5 should not use shopping and checkout prompts as its main content`() {
        val session = repository.getLessonSession(5)
        val sourceTexts = session.exercises.map { it.sourceText.lowercase() }

        assertFalse(sourceTexts.any { it.contains("сколько стоит") })
        assertFalse(sourceTexts.any { it.contains("картой или наличными") })
        assertFalse(sourceTexts.any { it.contains("вам нужен пакет") })
        assertFalse(sourceTexts.any { it.contains("вот ваша сдача") })
    }
}
