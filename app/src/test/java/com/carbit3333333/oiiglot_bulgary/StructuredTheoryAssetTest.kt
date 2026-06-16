package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.model.theory.StructuredTheoryLessonAsset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StructuredTheoryAssetTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `structured theory assets decode for both locales`() {
        val ruTheory = json.decodeFromString<List<StructuredTheoryLessonAsset>>(
            readAssetText("lesson_theory_ru.json"),
        )
        val ukTheory = json.decodeFromString<List<StructuredTheoryLessonAsset>>(
            readAssetText("lesson_theory_uk.json"),
        )

        assertTheoryAssetsAreValid(ruTheory)
        assertTheoryAssetsAreValid(ukTheory)
    }

    @Test
    fun `structured theory lesson 1 is present in both locales`() {
        val ruTheory = json.decodeFromString<List<StructuredTheoryLessonAsset>>(
            readAssetText("lesson_theory_ru.json"),
        )
        val ukTheory = json.decodeFromString<List<StructuredTheoryLessonAsset>>(
            readAssetText("lesson_theory_uk.json"),
        )

        val ruLesson = ruTheory.single { it.lessonId == 1 }
        val ukLesson = ukTheory.single { it.lessonId == 1 }

        assertTrue(ruLesson.blocks.size >= 10)
        assertTrue(ukLesson.blocks.size >= 10)
        assertTrue(ruLesson.blocks.flatMap { it.segments }.any { it.style.name == "Keyword" })
        assertTrue(ukLesson.blocks.flatMap { it.segments }.any { it.style.name == "Keyword" })
        assertTrue(ruLesson.blocks.flatMap { it.segments }.count { it.style.name == "Phrase" } >= 6)
        assertTrue(ukLesson.blocks.flatMap { it.segments }.count { it.style.name == "Phrase" } >= 6)
    }

    @Test
    fun `structured theory assets cover all 11 lessons`() {
        val ruTheory = json.decodeFromString<List<StructuredTheoryLessonAsset>>(
            readAssetText("lesson_theory_ru.json"),
        )
        val ukTheory = json.decodeFromString<List<StructuredTheoryLessonAsset>>(
            readAssetText("lesson_theory_uk.json"),
        )

        assertEquals((1..11).toList(), ruTheory.map { it.lessonId })
        assertEquals((1..11).toList(), ukTheory.map { it.lessonId })
        assertTrue(ruTheory.all { it.blocks.isNotEmpty() })
        assertTrue(ukTheory.all { it.blocks.isNotEmpty() })
        assertTrue(ruTheory.all { lesson -> lesson.blocks.flatMap { it.segments }.any { it.style.name == "Keyword" } })
        assertTrue(ukTheory.all { lesson -> lesson.blocks.flatMap { it.segments }.any { it.style.name == "Keyword" } })
    }

    private fun assertTheoryAssetsAreValid(theoryAssets: List<StructuredTheoryLessonAsset>) {
        assertTrue(theoryAssets.isNotEmpty())
        assertTrue(theoryAssets.all { it.lessonId > 0 })
        assertTrue(
            theoryAssets.all { lesson ->
                lesson.blocks.isNotEmpty() &&
                    lesson.blocks.all { block ->
                        block.segments.isNotEmpty() &&
                            block.segments.all { segment -> segment.text.isNotBlank() }
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
