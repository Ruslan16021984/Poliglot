package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonJsonAssetsTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `lessons json decodes expected lesson structure`() {
        val lessons = json.decodeFromString<List<Lesson>>(
            readAssetText("lessons.json")
        )

        assertEquals(8, lessons.size)
        assertEquals((1..8).toList(), lessons.map { it.id })
        assertTrue(lessons.all { it.title.isNotBlank() })
        assertTrue(lessons.all { it.subtitle.isNotBlank() })
        assertTrue(lessons.all { it.theory.isNotEmpty() })
        assertTrue(lessons.all { lesson ->
            lesson.theory.all { block ->
                !block.title.isNullOrBlank() && !block.text.isNullOrBlank()
            }
        })
    }

    @Test
    fun `lesson session json decodes expected migrated content`() {
        val assets = json.decodeFromString<LessonSessionAssets>(
            readAssetText("lesson_session_content.json")
        )

        assertEquals(6, assets.lesson3SubjectRu.size)
        assertEquals(10, assets.lesson3Verbs.size)
        assertEquals(12, assets.lesson4Items.size)
        assertEquals(12, assets.lesson7Templates.size)
        assertEquals(15, assets.lesson8Templates.size)

        assertTrue(assets.lesson3Verbs.all { it.past.keys == assets.lesson3SubjectRu.keys })
        assertTrue(assets.lesson3Verbs.all { it.ruPast.keys == assets.lesson3SubjectRu.keys })
        assertTrue(assets.lesson4Items.all { it.correctWords.isNotEmpty() })
        assertTrue(assets.lesson7Templates.all { it.bgWords.isNotEmpty() && it.ru.isNotBlank() })
        assertTrue(assets.lesson8Templates.all { it.bgWords.isNotEmpty() && it.ru.isNotBlank() })

        assertNotNull(assets.lesson7Templates.find { it.ru == "Ты видишь своего друга" })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Это самый прекрасный день в моей жизни" })
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
