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
        assertNotNull(lessons.find { it.id == 9 && it.title == "Урок 9" })
        assertNotNull(lessons.find { it.id == 10 && it.title == "Урок 10" })
    }

    @Test
    fun `lesson session json decodes expected migrated content`() {
        val assets = json.decodeFromString<LessonSessionAssets>(
            readAssetText("lesson_session_content.json"),
        )

        assertEquals(8, assets.lesson1Subjects.size)
        assertTrue(assets.lesson1Sentences.size >= 50)
        assertEquals(0, assets.lesson1Templates.size)
        assertEquals(7, assets.lesson1Verbs.size)
        assertEquals(8, assets.lesson3SubjectRu.size)
        assertEquals(12, assets.lesson3Verbs.size)
        assertEquals(100, assets.lesson4Items.size)
        assertEquals(20, assets.lesson7Templates.size)
        assertEquals(25, assets.lesson8Templates.size)
        assertEquals(20, assets.lesson9Numbers.size)
        assertEquals(5, assets.lesson9Objects.size)
        assertEquals(8, assets.lesson9Templates.size)
        assertEquals(17, assets.lesson10TimePhrases.size)
        assertEquals(12, assets.lesson10RoutineActions.size)
        assertEquals(3, assets.lesson10Intervals.size)
        assertEquals(7, assets.lesson10IntervalActions.size)
        assertEquals(2, assets.lesson10Templates.size)
        assertEquals(2, assets.lesson10IntervalTemplates.size)
        assertEquals(12, assets.lesson10QuestionActions.size)
        assertEquals(2, assets.lesson10QuestionTemplates.size)

        assertTrue(assets.lesson1Subjects.any { it.bg == "Тя" && it.ru == "Она" })
        assertTrue(assets.lesson1Subjects.any { it.bg == "То" && it.ru == "Оно" })
        assertTrue(assets.lesson1Subjects.any { it.bg == "Те" && it.ru == "Они" })
        assertTrue(assets.lesson1Templates.all { it.bgTokens.isNotEmpty() && it.ruPattern.isNotBlank() })
        assertTrue(assets.lesson1Verbs.all { it.objects.isNotEmpty() })
        assertTrue(assets.lesson1Verbs.all { it.formsBg.keys.size == assets.lesson1Subjects.size })
        assertTrue(assets.lesson1Verbs.all { it.formsRu.keys.size == assets.lesson1Subjects.size })
        assertTrue(assets.lesson1Verbs.any { it.formsBg["Аз"] == "обичам" })
        assertTrue(assets.lesson1Verbs.any { it.formsBg["Аз"] == "имам" && it.kind == "have" })

        assertTrue(assets.lesson3Verbs.all { it.past.keys == assets.lesson3SubjectRu.keys })
        assertTrue(assets.lesson3Verbs.all { it.ruPast.keys == assets.lesson3SubjectRu.keys })
        assertTrue(assets.lesson4Items.all { it.correctWords.isNotEmpty() })
        assertTrue(assets.lesson3SubjectRu["Тя"] == "Она")
        assertTrue(assets.lesson3SubjectRu["То"] == "Оно")
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "ходил(а)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "ел(а)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Аз"] == "учился(ась)" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["Тя"] == "была" })
        assertTrue(assets.lesson3Verbs.any { it.ruPast["То"] == "имело" })

        assertTrue(assets.lesson4Items.all { it.ru.trim().split(Regex("\\s+")).size >= 3 })
        assertTrue(assets.lesson4Items.all { !it.uk.isNullOrBlank() })
        assertTrue(assets.lesson4Items.none { it.correctWords.size < 3 })
        assertTrue(assets.lesson4Items.any { it.ru == "я хочу эту воду" })
        assertTrue(assets.lesson4Items.any { it.uk == "я хочу цю воду" })
        assertTrue(assets.lesson4Items.any { it.ru == "я люблю читать" })
        assertTrue(assets.lesson4Items.any { it.ru == "мы хотим работать" })
        assertTrue(assets.lesson4Items.any { it.ru == "мы хотим читать эту книгу" })
        assertTrue(assets.lesson4Items.any { it.ru == "он хочет эту работу" })
        assertTrue(assets.lesson4Items.any { it.ru == "она любит пить кофе" })
        assertTrue(assets.lesson4Items.any { it.ru == "она любит говорить" })
        assertTrue(assets.lesson4Items.any { it.ru == "оно хочет пить воду" })
        assertTrue(assets.lesson4Items.any { it.ru == "вы хотите читать" })
        assertTrue(assets.lesson4Items.any { it.ru == "вы любите пить кофе" })
        assertTrue(assets.lesson4Items.any { it.ru == "вы хотите читать эту книгу" })

        assertTrue(assets.lesson7Templates.all { it.bgWords.isNotEmpty() && it.ru.isNotBlank() })
        assertTrue(assets.lesson8Templates.all { it.bgWords.isNotEmpty() && it.ru.isNotBlank() })
        assertNotNull(assets.lesson7Templates.find { it.ru == "У меня есть своя книга" && "своята" in it.bgWords })
        assertNotNull(assets.lesson7Templates.find { it.ru == "Я даю тебе свою книгу" && it.bgWords.firstOrNull() == "Аз" })
        assertNotNull(assets.lesson7Templates.find { it.ru == "Это мой телефон" && "телефон" in it.bgWords })
        assertNotNull(assets.lesson7Templates.find { it.ru == "Это наша машина" && "кола" in it.bgWords })
        assertNotNull(assets.lesson7Templates.find { it.ru == "Я даю тебе свой телефон" && "телефон" in it.bgWords })
        assertNotNull(assets.lesson7Templates.find { it.ru == "Ты берёшь свою книгу" && "взимаш" in it.bgWords })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Эта книга интереснее той книги" && it.bgWords.takeLast(2) == listOf("онази", "книга") })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Моя машина быстрее твоей" && it.bgWords.takeLast(2) == listOf("твоята", "кола") })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Наш дом больше вашего дома" && it.bgWords.takeLast(2) == listOf("вашия", "дом") })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Этот магазин больше того магазина" && it.bgWords.takeLast(2) == listOf("онзи", "магазин") })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Наш город красивее вашего города" && it.bgWords.takeLast(2) == listOf("вашия", "град") })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Это самый большой магазин" && "най-големият" in it.bgWords })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Она самая быстрая" && it.bgWords == listOf("Тя", "е", "най-бързата") })
        assertTrue(
            assets.lesson9Numbers.all {
                it.bgMasculine.isNotBlank() &&
                    it.bgFeminine.isNotBlank() &&
                    it.bgNeuter.isNotBlank() &&
                    it.ruMasculine.isNotBlank() &&
                    it.ruFeminine.isNotBlank() &&
                    it.ruNeuter.isNotBlank()
            },
        )
        assertTrue(assets.lesson9Objects.all { it.singular.isNotBlank() && it.countForm.isNotBlank() })
        assertTrue(assets.lesson9Templates.all { it.bgTokens.isNotEmpty() && it.ruTokens.isNotEmpty() })
        assertTrue(assets.lesson10TimePhrases.all { it.bgTokens.isNotEmpty() && it.ruTokens.isNotEmpty() })
        assertTrue(assets.lesson10RoutineActions.all { it.bgTokens.isNotEmpty() && it.ruTokens.isNotEmpty() })
        assertTrue(assets.lesson10Intervals.all { it.bgFromTokens.isNotEmpty() && it.bgToTokens.isNotEmpty() })
        assertTrue(assets.lesson10Templates.all { it.bgTokens.isNotEmpty() && it.ruTokens.isNotEmpty() })
        assertTrue(assets.lesson10IntervalTemplates.all { it.bgTokens.isNotEmpty() && it.ruTokens.isNotEmpty() })
        assertTrue(assets.lesson10QuestionActions.all { it.bgTokens.isNotEmpty() && it.ruTokens.isNotEmpty() })
        assertTrue(assets.lesson10QuestionTemplates.all { it.bgTokens.isNotEmpty() && it.ruTokens.isNotEmpty() })

        assertNotNull(assets.lesson7Templates.find { it.ru == "Ты видишь своего друга" })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Это самый прекрасный день в моей жизни" })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Это самый тёплый день" })
        assertNotNull(assets.lesson8Templates.find { it.ru == "Он самый высокий в классе" })
        assertNotNull(assets.lesson9Templates.find { it.ruTokens.contains("тебе") })
        assertNotNull(assets.lesson9Templates.find { it.ruTokens.lastOrNull() == "?" })
        assertNotNull(assets.lesson9Objects.find { it.singular == "билет" && it.ruMany == "билетов" })
        assertNotNull(assets.lesson9Objects.find { it.singular == "чаша" && it.ruMany == "чашек" })
        assertNotNull(assets.lesson9Templates.find { it.ruTokens == listOf("Я", "покупаю", "{num}", "{object}") })
        assertNotNull(assets.lesson9Templates.find { it.bgTokens == listOf("Аз", "имам", "{num}", "{object}") })
        assertNotNull(assets.lesson9Numbers.find { it.value == 20 })
        assertNotNull(assets.lesson10TimePhrases.find { it.bgTokens.contains("във") })
        assertNotNull(assets.lesson10TimePhrases.find { it.bgTokens.contains("след") })
        assertNotNull(assets.lesson10TimePhrases.find { it.ruTokens.contains("четверг") })
        assertNotNull(assets.lesson10TimePhrases.find { it.ruTokens.contains("Ночью") })
        assertNotNull(assets.lesson10TimePhrases.find { it.ruTokens.contains("Перед") })
        assertNotNull(assets.lesson10TimePhrases.find { it.ruTokens.contains("дня") })
        assertNotNull(assets.lesson10RoutineActions.find { it.ruTokens.contains("завтракаю") })
        assertNotNull(assets.lesson10RoutineActions.find { it.bgTokens.contains("вечерям") })
        assertNotNull(assets.lesson10RoutineActions.find { it.bgTokens.contains("обядвам") })
        assertNotNull(assets.lesson10RoutineActions.find { it.ruTokens == listOf("читаю", "дома") })
        assertNotNull(assets.lesson10RoutineActions.find { it.ruTokens == listOf("иду", "домой") })
        assertNotNull(
            assets.lesson10RoutineActions.find {
                it.ruTokens == listOf("возвращаюсь", "домой") &&
                    it.bgTokens == listOf("се", "прибирам", "вкъщи")
            },
        )
        assertNotNull(assets.lesson10IntervalTemplates.find { it.ruTokens.contains("{from}") })
        assertNotNull(assets.lesson10QuestionTemplates.find { it.ruTokens.contains("Когда") })
        assertNotNull(assets.lesson10QuestionTemplates.find { it.bgTokens.contains("Кога") })
        assertNotNull(assets.lesson10QuestionTemplates.find { it.bgTokens.contains("колко") })
        assertNotNull(assets.lesson10IntervalActions.find { it.bgTokens.contains("обядвам") })
        assertNotNull(assets.lesson10IntervalActions.find { it.ruTokens == listOf("пью", "кофе") })
        assertNotNull(assets.lesson10QuestionActions.find { it.bgTokens.contains("обядваш") })
        assertNotNull(assets.lesson10QuestionActions.find { it.ruTokens == listOf("ты", "читаешь", "дома") })
        assertNotNull(assets.lesson10QuestionActions.find { it.ruTokens == listOf("ты", "идёшь", "домой") })
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
