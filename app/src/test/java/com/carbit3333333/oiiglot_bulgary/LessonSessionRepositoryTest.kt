package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionFactory
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonSessionRepositoryTest {

    private val repository = LessonSessionRepository()
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `lesson 1 session builds complete exercises`() {
        val session = repository.getLessonSession(1)
        val normalizedSourceTexts = session.exercises.map { it.sourceText.lowercase() }

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(session.exercises.any { it.sourceText.startsWith("Она ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Оно ") })
        assertTrue(session.exercises.any { it.correctAnswerWords.firstOrNull() == "Тя" })
        assertTrue(session.exercises.any { it.correctAnswerWords.firstOrNull() == "То" })
        assertTrue(session.exercises.any { "ще" in it.correctAnswerWords })
        assertTrue(session.exercises.any { "няма" in it.correctAnswerWords })
        assertTrue(session.exercises.any { it.sourceText.contains("будет") || it.sourceText.contains("будут") })
        assertTrue(session.exercises.any { it.correctAnswerWords.any { word -> word == "обичам" || word == "обича" } })
        assertTrue(session.exercises.any { it.correctAnswerWords.any { word -> word == "имам" || word == "има" } })
        assertTrue(normalizedSourceTexts.any { it.contains("у меня есть") || it.contains("у него есть") })
        assertFalse(session.exercises.any { it.sourceText.contains("будет не будет") })
        assertFalse(session.exercises.any { it.sourceText.contains("есть нет") })
        assertFalse(
            session.exercises.any {
                it.correctAnswerWords.firstOrNull() == "То" &&
                    (it.correctAnswerWords.any { word -> word == "имам" || word == "има" })
            },
        )
    }

    @Test
    fun `lesson 3 session uses migrated json data`() {
        val session = repository.getLessonSession(3)

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(session.exercises.any { "не" in it.correctAnswerWords })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Я ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Ты ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Он ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Она ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Оно ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Мы ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Вы ") })
        assertTrue(session.exercises.any { it.sourceText.startsWith("Они ") })
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("(а)") ||
                    it.sourceText.contains(" / шла") ||
                    it.sourceText.contains("(лась)")
            },
        )
        assertTrue(session.exercises.any { it.sourceText.contains("была") })
        assertTrue(session.exercises.any { it.sourceText.contains("было") })
    }

    @Test
    fun `lesson 4 session uses full sentence practice`() {
        val session = LessonSessionFactory.create(
            lessonId = 4,
            assets = loadLessonSessionAssets(),
        )
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.any { it.contains("я хочу эту книгу") })
        assertTrue(sourceTexts.any { it.startsWith("мы ") })
        assertTrue(sourceTexts.any { it.contains("он хочет эту работу") })
        assertTrue(sourceTexts.any { it.startsWith("вы ") })
        assertTrue(sourceTexts.any { it.startsWith("она ") })
        assertTrue(sourceTexts.any { it.startsWith("оно ") })
        assertTrue(bgWords.contains("работата"))
        assertTrue(bgWords.contains("книгата"))
        assertTrue(bgWords.any { it.startsWith("обич") })
        assertTrue(bgWords.any { it.startsWith("уч") })
        assertTrue(bgWords.any { it.startsWith("яд") || it.startsWith("ям") })
    }

    @Test
    fun `lesson 4 session avoids standalone prompt fragments`() {
        val session = LessonSessionFactory.create(
            lessonId = 4,
            assets = loadLessonSessionAssets(),
        )
        val standalonePrompts = setOf(
            "книга",
            "эта книга",
            "женщина",
            "эта женщина",
            "ребёнок",
            "этот ребёнок",
            "вода",
            "эта вода",
            "работа",
            "эта работа",
            "кофе",
            "этот кофе",
            "есть",
            "пить",
            "работать",
            "читать",
            "учиться",
            "говорить",
        )

        assertTrue(
            session.exercises.all { exercise ->
                val wordCount = exercise.sourceText.trim().split(Regex("\\s+")).size
                wordCount >= 3
            },
        )
        assertTrue(session.exercises.none { it.sourceText in standalonePrompts })
    }

    @Test
    fun `lesson 5 session avoids incorrect russian collocations`() {
        val session = repository.getLessonSession(5)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertFalse(sourceTexts.any { it.contains("учиться болгарский") })
        assertFalse(sourceTexts.any { it.contains("хочу учиться болгарский") })
        assertFalse(sourceTexts.any { it.contains("Я мне нужно") })
        assertFalse(sourceTexts.any { it.contains("Ты тебе нужно") })
        assertFalse(sourceTexts.any { it.contains("Он ему нужно") })
        assertFalse(sourceTexts.any { it.contains("Она ей нужно") })
        assertFalse(sourceTexts.any { it.contains("Оно ему нужно") })
        assertTrue(sourceTexts.any { it.contains("учиться дома") || it.contains("учиться в школе") })
        assertTrue(sourceTexts.any { it.contains("читать книгу") || it.contains("читать письмо") })
        assertTrue(sourceTexts.any { it.contains("идти в магазин") || it.contains("идти домой") })
        assertTrue(sourceTexts.any { it.startsWith("Ей нужно") || it.startsWith("Она ") })
        assertTrue(sourceTexts.any { it.startsWith("Ему нужно") || it.startsWith("Оно ") })
    }

    @Test
    fun `lesson 2 and 6 sessions cover feminine and neuter subjects`() {
        val lesson2 = repository.getLessonSession(2)
        val lesson6 = repository.getLessonSession(6)
        val lesson6Words = lesson6.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertTrue(lesson2.exercises.any { it.sourceText.startsWith("Она ") })
        assertTrue(lesson2.exercises.any { it.sourceText.startsWith("Оно ") })
        assertTrue(lesson2.exercises.any { it.correctAnswerWords.firstOrNull() == "Тя" })
        assertTrue(lesson2.exercises.any { it.correctAnswerWords.firstOrNull() == "То" })

        assertTrue(lesson6.exercises.any { it.sourceText.startsWith("Она ") })
        assertTrue(lesson6.exercises.any { it.sourceText.startsWith("Оно ") })
        assertTrue(lesson6.exercises.any { it.correctAnswerWords.firstOrNull() == "Тя" })
        assertTrue(lesson6.exercises.any { it.correctAnswerWords.firstOrNull() == "То" })
        assertTrue(lesson6Words.contains("в магазина"))
        assertTrue(lesson6Words.contains("в офиса"))
        assertTrue(lesson6Words.contains("при учителя"))
        assertTrue(lesson6Words.contains("с колегата"))
    }

    @Test
    fun `lesson 7 and 8 sessions use migrated template content`() {
        val assets = loadLessonSessionAssets()
        val lesson7 = LessonSessionFactory.create(lessonId = 7, assets = assets)
        val lesson8 = LessonSessionFactory.create(lessonId = 8, assets = assets)

        assertEquals(100, lesson7.exercises.size)
        assertEquals(100, lesson8.exercises.size)
        assertTrue(lesson7.exercises.all(::isValidExercise))
        assertTrue(lesson8.exercises.all(::isValidExercise))
        assertTrue(lesson7.exercises.any { it.sourceText.contains("своего друга") })
        assertTrue(lesson7.exercises.any { it.sourceText.contains("даю тебе свою книгу") })
        assertTrue(lesson7.exercises.any { it.correctAnswerWords.contains("своята") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("лучший день") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("той книги") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("твоей") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("вашего дома") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("того магазина") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("вашего города") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("самый большой магазин") })
        assertTrue(lesson8.exercises.any { it.sourceText.contains("Она самая быстрая") })
    }

    @Test
    fun `lesson 9 session builds number exercises`() {
        val session = repository.getLessonSession(9)

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(session.exercises.any { it.sourceText.contains("двадцать") })
        assertTrue(session.exercises.any { it.sourceText.contains("один") || it.sourceText.contains("одну") || it.sourceText.contains("одно") })
        assertTrue(session.exercises.any { it.sourceText.contains("телефона") })
        assertTrue(session.exercises.any { it.sourceText.contains("писем") })
        assertTrue(session.exercises.any { it.sourceText.contains("билета") || it.sourceText.contains("билетов") })
        assertTrue(session.exercises.any { it.sourceText.contains("чашки") || it.sourceText.contains("чашек") })
        assertTrue(session.exercises.any { it.sourceText.contains("покупаю") })
        assertTrue(session.exercises.any { it.sourceText.contains("У меня есть") })
        assertTrue(session.exercises.any { it.sourceText.contains("Мы видим") })
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("одн") ||
                    it.sourceText.contains("две") ||
                    it.sourceText.contains("три")
            },
        )
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.any { word ->
                    word in setOf(
                        "един",
                        "една",
                        "едно",
                        "два",
                        "две",
                        "три",
                        "десет",
                        "единадесет",
                        "двадесет",
                    )
                }
            },
        )
        assertTrue(session.exercises.any { it.sourceText.trim().endsWith("?") })
        assertTrue(session.exercises.any { "ли" in it.correctAnswerWords })
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.containsAll(listOf("Ти", "виждаш", "ли")) ||
                    it.correctAnswerWords.containsAll(listOf("Ти", "взимаш", "ли"))
            },
        )
    }

    @Test
    fun `lesson 10 session builds time routine exercises`() {
        val session = repository.getLessonSession(10)

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("понедельник") ||
                    it.sourceText.contains("вторник") ||
                    it.sourceText.contains("среду")
            },
        )
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.any { word ->
                    word in setOf("в", "във", "след", "преди", "от", "до", "сутрин", "вечер")
                }
            },
        )
        assertTrue(
            session.exercises.any {
                it.sourceText.contains("до пяти") ||
                    it.sourceText.contains("до трёх") ||
                    it.sourceText.contains("до двух")
            },
        )
        assertTrue(
            session.exercises.any {
                it.sourceText.startsWith("Когда ") || it.sourceText.startsWith("Во сколько ")
            },
        )
        assertTrue(
            session.exercises.any {
                it.correctAnswerWords.firstOrNull() == "Кога" ||
                    it.correctAnswerWords.take(3) == listOf("В", "колко", "часа")
            },
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
        assertTrue(bgWords.any { it in setOf("закусвам", "вечерям", "обядвам") })
        assertTrue(bgWords.any { it in setOf("чета", "отивам", "прибирам", "работя") })
        assertTrue(bgWords.contains("вкъщи"))
    }

    @Test
    fun `lesson 2 session separates places roles and demo phrases`() {
        val session = repository.getLessonSession(2)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.any { it.contains("дома") || it.contains("в школе") || it.contains("на работе") })
        assertTrue(sourceTexts.any { it.contains("врач") || it.contains("учитель") || it.contains("студент") || it.contains("коллега") })
        assertTrue(sourceTexts.any { it.startsWith("Это ") })
        assertTrue(bgWords.contains("вкъщи"))
        assertTrue(bgWords.contains("лекар"))
        assertTrue(bgWords.contains("Това"))
    }

    private fun isValidExercise(exercise: LessonExercise): Boolean {
        assertTrue(exercise.sourceText.isNotBlank())
        assertEquals("Переведите предложение", exercise.instruction)
        assertFalse(exercise.correctAnswerWords.isEmpty())
        assertEquals(8, exercise.availableWords.size)
        assertTrue(exercise.correctAnswerWords.distinct().all { it in exercise.availableWords })
        return true
    }

    private fun loadLessonSessionAssets(): LessonSessionAssets {
        val path = resolveAssetPath("lesson_session_content.json")
        return json.decodeFromString(String(Files.readAllBytes(path), StandardCharsets.UTF_8))
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
