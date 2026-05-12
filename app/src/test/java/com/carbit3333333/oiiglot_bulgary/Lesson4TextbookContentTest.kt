package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class Lesson4TextbookContentTest {

    private val repository = LessonSessionRepository()

    @Test
    fun `lesson 4 should include eat and drink verbs from textbook`() {
        val session = repository.getLessonSession(4)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertTrue(sourceTexts.any { it.contains("Я ем") })
        assertTrue(sourceTexts.any { it.contains("Я пью") })
        assertTrue(sourceTexts.any { it.contains("Мы едим фрукты") })
        assertTrue(sourceTexts.any { it.contains("Вы пьёте кофе") })
    }

    @Test
    fun `lesson 4 should keep textbook shopping and checkout phrases`() {
        val session = repository.getLessonSession(4)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertTrue(sourceTexts.any { it.contains("Сколько стоит?") })
        assertTrue(sourceTexts.any { it.contains("Сколько стоят?") })
        assertTrue(sourceTexts.any { it.contains("Сколько стоит один килограмм яблок?") })
        assertTrue(sourceTexts.any { it.contains("Можно один килограмм картофеля?") })
        assertTrue(sourceTexts.any { it.contains("Картой или наличными?") })
        assertTrue(sourceTexts.any { it.contains("Картой, пожалуйста.") })
        assertTrue(sourceTexts.any { it.contains("Вам нужен пакет?") })
        assertTrue(sourceTexts.any { it.contains("Вот ваша сдача.") })
    }
}
