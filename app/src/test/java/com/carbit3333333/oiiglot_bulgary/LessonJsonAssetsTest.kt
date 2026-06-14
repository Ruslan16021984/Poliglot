package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.TextbookLessonExerciseSetAsset
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonJsonAssetsTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `localized theory json decodes expected lesson structure`() {
        val lessonsRu = json.decodeFromString<List<Lesson>>(readAssetText("lessons_ru.json"))
        val lessonsUk = json.decodeFromString<List<Lesson>>(readAssetText("lessons_uk.json"))

        assertLessonsAreValid(lessonsRu)
        assertLessonsAreValid(lessonsUk)
    }

    @Test
    fun `textbook exercise json exists for every app lesson`() {
        (1..11).forEach { lessonId ->
            val exerciseSet = json.decodeFromString<TextbookLessonExerciseSetAsset>(
                readAssetText("textbook_exercises_lesson$lessonId.json"),
            )

            assertEquals(lessonId, exerciseSet.lessonApp)
            assertEquals(lessonId, exerciseSet.lessonBook)
            assertEquals("sentence_builder", exerciseSet.exerciseType)
            assertEquals("textbook", exerciseSet.source)
            assertTrue(exerciseSet.title.isNotBlank())
            assertTrue(exerciseSet.items.size >= 20)
            assertTrue(exerciseSet.items.all { it.bg.isNotBlank() })
            assertTrue(exerciseSet.items.all { it.ru.isNotBlank() })
            assertTrue(exerciseSet.items.all { it.uk.isNotBlank() })
            assertTrue(exerciseSet.items.all { it.correctWords.isNotEmpty() })
            assertTrue(exerciseSet.items.all { it.distractors.isNotEmpty() })
            assertTrue(exerciseSet.items.all { !it.hintRu.isNullOrBlank() })
            assertTrue(exerciseSet.items.all { !it.hintUk.isNullOrBlank() })
        }
    }

    @Test
    fun `lesson 7 has broad preposition practice`() {
        val exerciseSet = json.decodeFromString<TextbookLessonExerciseSetAsset>(
            readAssetText("textbook_exercises_lesson7.json"),
        )
        val words = exerciseSet.items.flatMap { item ->
            item.correctWords.map { it.lowercase() }
        }

        assertTrue("lesson 7 exercise pool", exerciseSet.items.size >= 100)
        assertTrue("lesson 7 в or във", words.count { it == "в" || it == "във" } >= 10)
        assertTrue("lesson 7 до", words.count { it == "до" } >= 8)
        assertTrue("lesson 7 от", words.count { it == "от" } >= 5)
        assertTrue("lesson 7 преди", words.count { it == "преди" } >= 5)
        assertTrue("lesson 7 през", words.count { it == "през" } >= 10)
        assertTrue("lesson 7 след", words.count { it == "след" } >= 5)
    }

    @Test
    fun `lesson 10 practices transport verbs and route prepositions`() {
        val exerciseSet = json.decodeFromString<TextbookLessonExerciseSetAsset>(
            readAssetText("textbook_exercises_lesson10.json"),
        )
        val words = exerciseSet.items.flatMap { item ->
            item.correctWords.map { it.lowercase() }
        }

        assertEquals("lesson 10 exercise pool", 100, exerciseSet.items.size)
        assertTrue("lesson 10 excludes basic Това prompts", exerciseSet.items.none { it.bg.startsWith("Това") })
        assertTrue("lesson 10 тръгва", words.count { it == "тръгва" } >= 8)
        assertTrue("lesson 10 заминава", words.count { it == "заминава" } >= 6)
        assertTrue("lesson 10 спира", words.count { it == "спира" } >= 8)
        assertTrue("lesson 10 пристига", words.count { it == "пристига" } >= 8)
        assertTrue("lesson 10 от", words.count { it == "от" } >= 10)
        assertTrue("lesson 10 за", words.count { it == "за" } >= 15)
        assertTrue("lesson 10 в", words.count { it == "в" } >= 10)
        assertTrue("lesson 10 на", words.count { it == "на" } >= 10)
        assertTrue("lesson 10 с or със", words.count { it == "с" || it == "със" } >= 8)
        assertTrue("lesson 10 first bus question", exerciseSet.items.any { "първият автобус" in it.bg })
        assertTrue("lesson 10 last bus question", exerciseSet.items.any { "последният автобус" in it.bg })
        assertTrue("lesson 10 next bus question", exerciseSet.items.any { "следващият автобус" in it.bg })
        assertTrue("lesson 10 ticket count question", exerciseSet.items.any { "Колко билета" in it.bg })
        assertTrue("lesson 10 delay question", exerciseSet.items.any { "Колко минути закъснява" in it.bg })
        assertTrue("lesson 10 ticket price question", exerciseSet.items.any { "Колко струва билетът" in it.bg })
        assertTrue("lesson 10 railway track question", exerciseSet.items.any { "коловоз" in it.bg })
        assertTrue("lesson 10 bus sector question", exerciseSet.items.any { "сектор" in it.bg })
    }

    private fun assertLessonsAreValid(lessons: List<Lesson>) {
        assertEquals(11, lessons.size)
        assertEquals((1..11).toList(), lessons.map { it.id })
        assertTrue(lessons.all { it.title.isNotBlank() })
        assertTrue(lessons.all { it.subtitle.isNotBlank() })
        assertTrue(lessons.all { it.theory.isNotEmpty() })
        assertTrue(
            lessons.all { lesson ->
                lesson.theory.all { block ->
                    !block.title.isNullOrBlank() && !block.text.isNullOrBlank()
                }
            },
        )
    }

    private fun readAssetText(fileName: String): String {
        val path = resolveAssetPath(fileName)
        assertTrue("Asset file not found: $fileName", Files.exists(path))
        return String(Files.readAllBytes(path), StandardCharsets.UTF_8)
    }

    private fun resolveAssetPath(fileName: String): Path {
        val directPath = Paths.get("src", "main", "assets", fileName)
        if (Files.exists(directPath)) {
            return directPath
        }

        val rootPath = Paths.get("app", "src", "main", "assets", fileName)
        if (Files.exists(rootPath)) {
            return rootPath
        }

        return directPath
    }
}
