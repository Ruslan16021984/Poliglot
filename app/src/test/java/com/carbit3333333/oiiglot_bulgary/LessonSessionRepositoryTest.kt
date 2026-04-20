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

    @Test
    fun `lesson 9 session builds number exercises`() {
        val session = repository.getLessonSession(9)

        assertEquals(60, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("одн") ||
                    it.sourceText.contains("две") ||
                    it.sourceText.contains("три")
            }
        )
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.any { word ->
                    word in setOf("един", "една", "едно", "два", "две", "три", "десет", "единадесет", "двадесет")
                }
            }
        )
        assertTrue(session.exercises.any { it.sourceText.trim().endsWith("?") })
        assertTrue(session.exercises.any { "ли" in it.correctAnswerWords })
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.containsAll(listOf("Ти", "виждаш", "ли")) ||
                    it.correctAnswerWords.containsAll(listOf("Ти", "взимаш", "ли"))
            }
        )
    }

    @Test
    fun `lesson 10 session builds time routine exercises`() {
        val session = repository.getLessonSession(10)

        assertEquals(60, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("понедельник") ||
                    it.sourceText.contains("вторник") ||
                    it.sourceText.contains("среду")
            }
        )
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.any { word ->
                    word in setOf("в", "във", "след", "преди", "от", "до", "сутрин", "вечер")
                }
            }
        )
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("до пяти") ||
                    it.sourceText.contains("до трёх") ||
                    it.sourceText.contains("до двух")
            }
        )
        assertTrue(
            session.exercises.any {
                it.sourceText.startsWith("Когда ") || it.sourceText.startsWith("Во сколько ")
            }
        )
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.firstOrNull() == "Кога" ||
                    it.correctAnswerWords.take(3) == listOf("В", "колко", "часа")
            }
        )
    }

    @Test
    fun `lesson 10 session covers richer daily routine vocabulary`() {
        val session = repository.getLessonSession(10)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertTrue(sourceTexts.any { it.contains("четверг") })
        assertTrue(sourceTexts.any { it.contains("пятницу") })
        assertTrue(sourceTexts.any { it.contains("Ночью") })
        assertTrue(sourceTexts.any { it.contains("завтракаю") })
        assertTrue(sourceTexts.any { it.contains("ужинаю") })
        assertTrue(bgWords.contains("закусвам"))
        assertTrue(bgWords.contains("вечерям"))
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
