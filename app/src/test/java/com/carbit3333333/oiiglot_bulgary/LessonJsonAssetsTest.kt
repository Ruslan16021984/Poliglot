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
        assertNotNull(lessons.find { it.id == 9 })
        assertNotNull(lessons.find { it.id == 10 })
        assertNotNull(lessons.find { it.id == 11 })
    }

    @Test
    fun `lesson session json decodes expected migrated content`() {
        val assets = json.decodeFromString<LessonSessionAssets>(
            readAssetText("lesson_session_content.json"),
        )

        assertEquals(8, assets.lesson1Subjects.size)
        assertTrue(assets.lesson1Sentences.size >= 100)
        assertTrue(assets.lesson1Sentences.all { it.ru.isNotBlank() })
        assertEquals(100, assets.lesson2Sentences.size)
        assertTrue(assets.lesson2Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(100, assets.lesson3Sentences.size)
        assertTrue(assets.lesson3Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(100, assets.lesson5Sentences.size)
        assertTrue(assets.lesson5Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(100, assets.lesson6Sentences.size)
        assertTrue(assets.lesson6Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(20, assets.lesson7Sentences.size)
        assertTrue(assets.lesson7Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(25, assets.lesson8Sentences.size)
        assertTrue(assets.lesson8Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(100, assets.lesson9Sentences.size)
        assertTrue(assets.lesson9Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(100, assets.lesson10Sentences.size)
        assertTrue(assets.lesson10Sentences.all { !it.uk.isNullOrBlank() })
        assertEquals(0, assets.lesson1Templates.size)
        assertEquals(7, assets.lesson1Verbs.size)

        assertEquals(8, assets.lesson3SubjectRu.size)
        assertEquals(12, assets.lesson3Verbs.size)
        assertTrue(assets.lesson3Verbs.all { it.past.keys == assets.lesson3SubjectRu.keys })
        assertTrue(assets.lesson3Verbs.all { it.ruPast.keys == assets.lesson3SubjectRu.keys })

        assertTrue(assets.lesson4Items.size >= 100)
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
        assertTrue(assets.lesson1Sentences.any { it.ru == "Я смотрю фильм" })
        assertTrue(assets.lesson1Sentences.any { it.ru == "У меня есть книга" })

        assertTrue(assets.lesson3SubjectRu["Тя"] == "Она")
        assertTrue(assets.lesson3SubjectRu["То"] == "Оно")
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "ходил(а)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "ел(а)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "учился(ась)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Тя"] == "была" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["То"] == "имело" })

        assertTrue(assets.lesson4Items.any { it.ru == "я хочу эту воду" && it.uk == "я хочу цю воду" })
        assertTrue(assets.lesson4Items.any { it.ru == "вы хотите читать эту книгу" })
        assertTrue(assets.lesson4Items.any { it.ru == "мне нравится эта книга" && it.uk == "мені подобається ця книжка" })
        assertTrue(
            assets.lesson2Sentences.any {
                it.correctWords == listOf("Това", "е", "хляб") &&
                    it.ru == "Это хлеб" &&
                    it.uk == "Це хліб"
            },
        )
        assertTrue(
            assets.lesson2Sentences.any {
                it.correctWords == listOf("Това", "море", "ли", "е") &&
                    it.ru == "Это море?" &&
                    it.uk == "Це море?"
            },
        )
        assertTrue(
            assets.lesson3Sentences.any {
                it.ru == "Я делал(а)" &&
                    it.correctWords == listOf("Аз", "правих")
            },
        )
        assertTrue(
            assets.lesson3Sentences.any {
                it.ru == "Она была" &&
                    it.correctWords == listOf("Тя", "беше")
            },
        )
        assertTrue(
            assets.lesson3Sentences.any {
                it.ru == "Оно не имело" &&
                    it.correctWords == listOf("То", "не", "имаше")
            },
        )
        assertTrue(
            assets.lesson5Sentences.any {
                it.ru == "Я могу смотреть фильм" &&
                    it.uk == "Я можу дивитися фільм" &&
                    it.correctWords == listOf("Аз", "мога", "да", "гледам", "филм")
            },
        )
        assertTrue(
            assets.lesson5Sentences.any {
                it.ru == "мне не нужно идти в магазин" &&
                    it.uk == "мені не потрібно йти в магазин" &&
                    it.correctWords == listOf("Аз", "не", "трябва", "да", "отивам", "в магазина")
            },
        )
        assertTrue(
            assets.lesson6Sentences.any {
                it.ru == "Я в городе" &&
                    it.uk == "Я в місті" &&
                    it.correctWords == listOf("Аз", "съм", "в града")
            },
        )
        assertTrue(
            assets.lesson6Sentences.any {
                it.ru == "Она в офисе?" &&
                    it.uk == "Вона в офісі?" &&
                    it.correctWords == listOf("Тя", "в офиса", "ли", "е")
            },
        )

        assertNotNull(assets.lesson7Templates.find { it.ru == "У меня есть своя книга" && "своята" in it.bgWords })
        assertNotNull(
            assets.lesson7Sentences.find {
                it.ru == "Я даю тебе свою книгу" &&
                    it.uk == "Я даю тобі свою книгу" &&
                    it.correctWords == listOf("Аз", "ти", "давам", "своята", "книга")
            },
        )
        assertNotNull(
            assets.lesson8Sentences.find {
                it.ru == "Эта книга интереснее той книги" &&
                    it.uk == "Ця книга цікавіша за ту книгу" &&
                    it.correctWords == listOf("Тази", "книга", "е", "по-интересна", "от", "онази", "книга")
            },
        )
        assertNotNull(
            assets.lesson9Sentences.find {
                it.ru == "У меня есть семь книг" &&
                    it.uk == "У мене є сім книг" &&
                    it.correctWords == listOf("Аз", "имам", "седем", "книги")
            },
        )
        assertNotNull(
            assets.lesson10Sentences.find {
                it.ru == "В понедельник работаю" &&
                    it.uk == "У понеділок працюю" &&
                    it.correctWords == listOf("В", "понеделник", "работя")
            },
        )
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

        assertNotNull(assets.lesson9Numbers.find { it.value == 4 && it.ruMasculine == "четыре" })
        assertNotNull(assets.lesson9Objects.find { it.singular == "билет" && it.ruSingular == "билет" })
        assertNotNull(assets.lesson9Templates.find { it.ruTokens == listOf("У", "меня", "есть", "{num}", "{object}") })
        assertNotNull(assets.lesson10TimePhrases.find { it.ruTokens == listOf("В", "понедельник") })
        assertNotNull(assets.lesson10RoutineActions.find { it.ruTokens == listOf("пью", "кофе") })
        assertNotNull(assets.lesson10Intervals.find { it.ruFromTokens == listOf("с", "девяти") && it.ruToTokens == listOf("до", "пяти") })
        assertNotNull(assets.lesson10QuestionTemplates.find { it.ruTokens == listOf("Когда", "{action}") })
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
