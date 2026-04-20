package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import org.junit.Test

class LessonSessionRepositoryTest {

    @Test
    fun `generate and print 10 lesson 1 exercises`() {
        val repository = LessonSessionRepository()
        val session = repository.getLessonSession(1)

        println("\n=== LESSON 1: First 10 exercises ===\n")

        session.exercises.take(10).forEachIndexed { index, exercise ->
            println("📝 Exercise ${index + 1}")
            println("   Russian: ${exercise.sourceText}")
            println("   Instruction: ${exercise.instruction}")
            println("   ✅ Correct answer: ${exercise.correctAnswerWords.joinToString(" ")}")
            println("   📦 Available words (${exercise.availableWords.size}): ${exercise.availableWords.joinToString(", ")}")

            // Check if all correct words are in available words
            val missingWords = exercise.correctAnswerWords.filterNot { it in exercise.availableWords }
            if (missingWords.isNotEmpty()) {
                println("   ❌ ERROR: Missing words in available: $missingWords")
            } else {
                println("   ✓ All correct words are available")
            }
            println()
        }
    }

    @Test
    fun `generate and print 10 lesson 2 exercises`() {
        val repository = LessonSessionRepository()
        val session = repository.getLessonSession(2)

        println("\n=== LESSON 2: First 10 exercises ===\n")

        session.exercises.take(10).forEachIndexed { index, exercise ->
            println("📝 Exercise ${index + 1}")
            println("   Russian: ${exercise.sourceText}")
            println("   Instruction: ${exercise.instruction}")
            println("   ✅ Correct answer: ${exercise.correctAnswerWords.joinToString(" ")}")
            println("   📦 Available words (${exercise.availableWords.size}): ${exercise.availableWords.joinToString(", ")}")

            val missingWords = exercise.correctAnswerWords.filterNot { it in exercise.availableWords }
            if (missingWords.isNotEmpty()) {
                println("   ❌ ERROR: Missing words in available: $missingWords")
            } else {
                println("   ✓ All correct words are available")
            }
            println()
        }
    }

    @Test
    fun `generate and print 10 lesson 3 exercises`() {
        val repository = LessonSessionRepository()
        val session = repository.getLessonSession(3)

        println("\n=== LESSON 3: First 10 exercises ===\n")

        session.exercises.take(10).forEachIndexed { index, exercise ->
            println("📝 Exercise ${index + 1}")
            println("   Russian: ${exercise.sourceText}")
            println("   Instruction: ${exercise.instruction}")
            println("   ✅ Correct answer: ${exercise.correctAnswerWords.joinToString(" ")}")
            println("   📦 Available words (${exercise.availableWords.size}): ${exercise.availableWords.joinToString(", ")}")

            val missingWords = exercise.correctAnswerWords.filterNot { it in exercise.availableWords }
            if (missingWords.isNotEmpty()) {
                println("   ❌ ERROR: Missing words in available: $missingWords")
            } else {
                println("   ✓ All correct words are available")
            }
            println()
        }
    }
}
