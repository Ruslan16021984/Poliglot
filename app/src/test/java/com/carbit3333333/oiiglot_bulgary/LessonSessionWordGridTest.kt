package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.ui.lessons.selectedWordSlots
import org.junit.Assert.assertEquals
import org.junit.Test

class LessonSessionWordGridTest {

    @Test
    fun `selected words keep their original grid slots`() {
        val slots = selectedWordSlots(
            words = listOf("Аз", "съм", "тук", "днес"),
            selectedWords = listOf("съм", "днес"),
        )

        assertEquals(listOf(false, true, false, true), slots)
    }

    @Test
    fun `only selected occurrences are disabled for duplicate words`() {
        val slots = selectedWordSlots(
            words = listOf("да", "не", "да", "ли"),
            selectedWords = listOf("да"),
        )

        assertEquals(listOf(true, false, false, false), slots)
    }
}
