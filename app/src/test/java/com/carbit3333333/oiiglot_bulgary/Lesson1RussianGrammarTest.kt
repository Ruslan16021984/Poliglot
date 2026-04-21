package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Lesson1RussianGrammarTest {

    private val repository = LessonSessionRepository()

    @Test
    fun `lesson 1 should not use present tense verb after russian future auxiliary`() {
        val session = repository.getLessonSession(1)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertFalse(sourceTexts.any { it.contains("буду смотрю") })
        assertFalse(sourceTexts.any { it.contains("будешь работаешь") })
        assertFalse(sourceTexts.any { it.contains("будет учится") })
        assertFalse(sourceTexts.any { it.contains("будем говорим") })
        assertFalse(sourceTexts.any { it.contains("будете пьёте") })
        assertFalse(sourceTexts.any { it.contains("будут смотрят") })
    }

    @Test
    fun `lesson 1 should avoid incorrect russian object government`() {
        val session = repository.getLessonSession(1)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertFalse(sourceTexts.any { it.contains("учусь болгарский") })
        assertFalse(sourceTexts.any { it.contains("учишься болгарский") })
        assertFalse(sourceTexts.any { it.contains("учится болгарский") })
        assertTrue(sourceTexts.none { it.contains("говорю по-болгарски здесь") })
    }

    @Test
    fun `lesson 1 should use more natural russian collocations`() {
        val session = repository.getLessonSession(1)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertFalse(sourceTexts.any { it.contains("учусь здесь") })
        assertFalse(sourceTexts.any { it.contains("учишься здесь") })
        assertFalse(sourceTexts.any { it.contains("говорю здесь") })
        assertTrue(sourceTexts.any { it.contains("учусь дома") || it.contains("учимся дома") })
        assertTrue(sourceTexts.any { it.contains("говорю медленно") || it.contains("говорят медленно") })
    }
}
