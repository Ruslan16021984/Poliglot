package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.model.Lesson
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
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
            readAssetText("lessons.json"),
        )

        assertEquals(10, lessons.size)
        assertEquals((1..10).toList(), lessons.map { it.id })
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
        assertNotNull(lessons.find { it.id == 9 })
        assertNotNull(lessons.find { it.id == 10 })
    }

    @Test
    fun `lesson session json decodes expected migrated content`() {
        val assets = json.decodeFromString<LessonSessionAssets>(
            readAssetText("lesson_session_content.json"),
        )

        assertEquals(8, assets.lesson1Subjects.size)
        assertTrue(assets.lesson1Sentences.size >= 100)
        assertTrue(assets.lesson1Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(0, assets.lesson1Templates.size)
        assertEquals(7, assets.lesson1Verbs.size)

        assertEquals(8, assets.lesson3SubjectRu.size)
        assertEquals(8, assets.lesson3SubjectUk.size)
        assertEquals(12, assets.lesson3Verbs.size)
        assertTrue(assets.lesson3Verbs.all { it.past.keys == assets.lesson3SubjectRu.keys })
        assertTrue(assets.lesson3Verbs.all { it.ruPast.keys == assets.lesson3SubjectRu.keys })
        assertTrue(assets.lesson3Verbs.all { it.ukPast.size == assets.lesson3SubjectUk.size })
        assertTrue(assets.lesson3Verbs.all { it.ukPast.values.none { value -> value.isBlank() } })

        assertEquals(100, assets.lesson4Items.size)
        assertTrue(assets.lesson4Items.all { !it.uk.isNullOrBlank() })

        assertEquals(20, assets.lesson7Templates.size)
        assertEquals(25, assets.lesson8Templates.size)
        assertTrue(assets.lesson7Templates.all { !it.uk.isNullOrBlank() })
        assertTrue(assets.lesson8Templates.all { !it.uk.isNullOrBlank() })

        assertEquals(20, assets.lesson9Numbers.size)
        assertEquals(5, assets.lesson9Objects.size)
        assertEquals(8, assets.lesson9Templates.size)
        assertEquals(17, assets.lesson10TimePhrases.size)
        assertEquals(12, assets.lesson10RoutineActions.size)
        assertEquals(3, assets.lesson10Intervals.size)
        assertEquals(7, assets.lesson10IntervalActions.size)
        assertEquals(12, assets.lesson10QuestionActions.size)
        assertEquals(2, assets.lesson10Templates.size)
        assertEquals(2, assets.lesson10IntervalTemplates.size)
        assertEquals(2, assets.lesson10QuestionTemplates.size)

        assertTrue(assets.lesson1Subjects.any { it.bg == "Тя" && it.ru == "Она" })
        assertTrue(assets.lesson1Subjects.any { it.bg == "То" && it.ru == "Оно" })
        assertTrue(assets.lesson1Subjects.any { it.bg == "Те" && it.ru == "Они" })
        assertTrue(assets.lesson1Verbs.any { it.formsBg["Аз"] == "обичам" })
        assertTrue(assets.lesson1Verbs.any { it.formsBg["Аз"] == "имам" && it.kind == "have" })
        assertTrue(assets.lesson1Sentences.any { it.ru == "Я смотрю фильм" && it.uk == "Я дивлюся фільм" })
        assertTrue(assets.lesson1Sentences.any { it.ru == "У меня есть книга" && it.uk == "У мене є книга" })

        assertTrue(assets.lesson3SubjectRu["Тя"] == "Она")
        assertTrue(assets.lesson3SubjectRu["То"] == "Оно")
        assertTrue(assets.lesson3SubjectUk["Той"] == "Він")
        assertTrue(assets.lesson3SubjectUk["Те"] == "Вони")
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "ходил(а)" })
        assertTrue(assets.lesson3Verbs.any { it.ukPast["Аз"] == "ходив(ла)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "ел(а)" })
        assertTrue(assets.lesson3Verbs.any { it.ukPast["Аз"] == "їв(ла)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "учился(ась)" })
        assertTrue(assets.lesson3Verbs.any { it.ukPast["Аз"] == "вчився(лася)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Тя"] == "была" })
        assertTrue(assets.lesson3Verbs.any { it.ukPast["Тя"] == "була" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["То"] == "имело" })
        assertTrue(assets.lesson3Verbs.any { it.ukPast["То"] == "мало" })

        assertTrue(assets.lesson4Items.any { it.ru == "я хочу эту воду" && it.uk == "я хочу цю воду" })
        assertTrue(assets.lesson4Items.any { it.ru == "вы хотите читать эту книгу" })

        assertNotNull(assets.lesson7Templates.find { it.ru == "У меня есть своя книга" && "своята" in it.bgWords })
        assertNotNull(assets.lesson8Templates.find { it.uk == "Ця книга цікавіша за ту книгу" })

        assertTrue(assets.lesson9Numbers.all { it.ukMasculine.isNotBlank() && it.ukFeminine.isNotBlank() && it.ukNeuter.isNotBlank() })
        assertTrue(assets.lesson9Objects.all { it.ukSingular.isNotBlank() && it.ukPlural.isNotBlank() && it.ukMany.isNotBlank() })
        assertTrue(assets.lesson9Templates.all { it.ukTokens.isNotEmpty() })
        assertTrue(assets.lesson10TimePhrases.all { it.ukTokens.isNotEmpty() })
        assertTrue(assets.lesson10RoutineActions.all { it.ukTokens.isNotEmpty() })
        assertTrue(assets.lesson10Intervals.all { it.ukFromTokens.isNotEmpty() && it.ukToTokens.isNotEmpty() })
        assertTrue(assets.lesson10IntervalActions.all { it.ukTokens.isNotEmpty() })
        assertTrue(assets.lesson10QuestionActions.all { it.ukTokens.isNotEmpty() })
        assertTrue(assets.lesson10Templates.all { it.ukTokens.isNotEmpty() })
        assertTrue(assets.lesson10IntervalTemplates.all { it.ukTokens.isNotEmpty() })
        assertTrue(assets.lesson10QuestionTemplates.all { it.ukTokens.isNotEmpty() })

        assertNotNull(assets.lesson9Numbers.find { it.value == 4 && it.ukMasculine == "чотири" })
        assertNotNull(assets.lesson9Objects.find { it.singular == "билет" && it.ukSingular == "квиток" })
        assertNotNull(assets.lesson9Templates.find { it.ukTokens == listOf("У", "мене", "є", "{num}", "{object}") })
        assertNotNull(assets.lesson10TimePhrases.find { it.ukTokens == listOf("У", "понеділок") })
        assertNotNull(assets.lesson10RoutineActions.find { it.ukTokens == listOf("п'ю", "каву") })
        assertNotNull(assets.lesson10Intervals.find { it.ukFromTokens == listOf("з", "дев'ятої") && it.ukToTokens == listOf("до", "п'ятої") })
        assertNotNull(assets.lesson10QuestionTemplates.find { it.ukTokens == listOf("Коли", "{action}") })
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
