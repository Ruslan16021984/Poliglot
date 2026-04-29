package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.LessonSessionRepository
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionAssets
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonSessionFactory
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.TextbookLessonExerciseSetAsset
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
    fun `lesson 1 session uses textbook exercise source`() {
        val session = repository.getLessonSession(1)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.contains("Здравствуйте, я Хасан."))
        assertTrue(sourceTexts.contains("Откуда Вы?"))
        assertTrue(sourceTexts.contains("Я из Болгарии."))
        assertTrue(sourceTexts.contains("Я из Сирии."))
        assertTrue(sourceTexts.contains("Я беженец."))
        assertTrue(sourceTexts.contains("Она болгарка."))
        assertTrue(bgWords.contains("Здравейте"))
        assertTrue(bgWords.contains("съм"))
        assertTrue(bgWords.contains("България"))
        assertTrue(bgWords.contains("бежанец"))
        assertFalse(sourceTexts.any { "будет не будет" in it })
        assertFalse(sourceTexts.any { "есть нет" in it })
    }

    @Test
    fun `lesson 2 session separates places roles and demo phrases`() {
        val session = repository.getLessonSession(2)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()
        val firstWords = session.exercises.mapNotNull { it.correctAnswerWords.firstOrNull() }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(bgWords.contains("Това"))
        assertTrue(firstWords.containsAll(setOf("Аз", "Ти", "Той", "Тя", "То", "Ние", "Вие", "Те", "Това")))
        assertTrue(bgWords.any { it in setOf("вкъщи", "училище", "работа", "в библиотеката", "на улицата", "в болницата") })
        assertTrue(bgWords.any { it in setOf("лекар", "учител", "студент", "колега", "директор", "инженер", "продавач") })
        assertTrue(bgWords.any { it in setOf("магазин", "библиотека", "дете", "хляб", "книга", "кафе") })
        assertTrue(sourceTexts.any { it.startsWith("Это ") })
    }

    @Test
    fun `lesson 3 session uses migrated json data`() {
        val session = repository.getLessonSession(3)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(session.exercises.any { "не" in it.correctAnswerWords })
        assertTrue(sourceTexts.any { it.startsWith("Я ") })
        assertTrue(sourceTexts.any { it.startsWith("Ты ") })
        assertTrue(sourceTexts.any { it.startsWith("Он ") })
        assertTrue(sourceTexts.any { it.startsWith("Она ") })
        assertTrue(sourceTexts.any { it.startsWith("Оно ") })
        assertTrue(sourceTexts.any { it.startsWith("Мы ") })
        assertTrue(sourceTexts.any { it.startsWith("Вы ") })
        assertTrue(sourceTexts.any { it.startsWith("Они ") })
        assertTrue(sourceTexts.any { "была" in it })
        assertTrue(sourceTexts.any { "было" in it })
    }

    @Test
    fun `lesson 4 session uses textbook exercise source`() {
        val session = LessonSessionFactory.create(
            lessonId = 4,
            assets = loadLessonSessionAssets(),
            textbookExercises = loadTextbookExerciseSet(4),
        )
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.contains("Я хочу хлеб."))
        assertTrue(sourceTexts.contains("Я хочу воду."))
        assertTrue(sourceTexts.contains("Я люблю кофе."))
        assertTrue(sourceTexts.contains("Мне нравится чай."))
        assertTrue(sourceTexts.contains("Ты любишь сок?"))
        assertTrue(sourceTexts.contains("Ты хочешь чай?"))
        assertTrue(bgWords.any { it in setOf("Искам", "Искаш") })
        assertTrue(bgWords.any { it in setOf("Обичам", "Обичаш") })
        assertTrue(bgWords.contains("Харесвам"))
        assertTrue(bgWords.contains("хляб"))
        assertTrue(bgWords.contains("кафе"))
    }

    @Test
    fun `lesson 4 session avoids standalone prompt fragments`() {
        val session = LessonSessionFactory.create(
            lessonId = 4,
            assets = loadLessonSessionAssets(),
            textbookExercises = loadTextbookExerciseSet(4),
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

        assertTrue(session.exercises.all { it.sourceText.trim().split(Regex("\\s+")).size >= 2 })
        assertTrue(session.exercises.none { it.sourceText in standalonePrompts })
    }

    @Test
    fun `lesson 5 session avoids incorrect russian collocations`() {
        val session = repository.getLessonSession(5)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertFalse(sourceTexts.any { "учиться болгарский" in it })
        assertFalse(sourceTexts.any { "Я мне нужно" in it })
        assertFalse(sourceTexts.any { "Ты тебе нужно" in it })
        assertFalse(sourceTexts.any { "Он ему нужно" in it })
        assertFalse(sourceTexts.any { "Она ей нужно" in it })
        assertTrue(sourceTexts.any { "учиться дома" in it || "учиться в школе" in it })
        assertTrue(sourceTexts.any { "читать книгу" in it || "читать письмо" in it })
        assertTrue(sourceTexts.any { "идти в магазин" in it || "идти домой" in it })
    }

    @Test
    fun `lesson 2 and 6 sessions cover feminine and neuter subjects`() {
        val lesson2 = repository.getLessonSession(2)
        val lesson6 = repository.getLessonSession(6)
        val lesson2FirstWords = lesson2.exercises.mapNotNull { it.correctAnswerWords.firstOrNull() }.toSet()
        val lesson6FirstWords = lesson6.exercises.mapNotNull { it.correctAnswerWords.firstOrNull() }.toSet()
        val lesson6Words = lesson6.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertTrue(lesson2FirstWords.contains("Тя"))
        assertTrue(lesson2FirstWords.contains("То"))
        assertTrue(lesson6.exercises.any { it.sourceText.startsWith("Она ") })
        assertTrue(lesson6.exercises.any { it.sourceText.startsWith("Оно ") })
        assertTrue(lesson6FirstWords.contains("Тя"))
        assertTrue(lesson6FirstWords.contains("То"))
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
        assertTrue(lesson7.exercises.any { "свою книгу" in it.sourceText })
        assertTrue(lesson7.exercises.any { "даю тебе" in it.sourceText })
        assertTrue(lesson7.exercises.any { it.correctAnswerWords.contains("своята") })
        assertTrue(lesson8.exercises.any { "той книги" in it.sourceText || "того магазина" in it.sourceText })
        assertTrue(lesson8.exercises.any { "твоей" in it.sourceText || "вашего дома" in it.sourceText })
        assertTrue(lesson8.exercises.any { "самый большой магазин" in it.sourceText || "самая быстрая" in it.sourceText })
    }

    @Test
    fun `lesson 9 session builds number exercises`() {
        val session = repository.getLessonSession(9)
        val sourceTexts = session.exercises.map { it.sourceText }

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.any { "один" in it || "одну" in it || "одно" in it })
        assertTrue(sourceTexts.any { "билета" in it || "билетов" in it || "чашки" in it || "чашек" in it })
        assertTrue(sourceTexts.any { "покупаю" in it })
        assertTrue(sourceTexts.any { "У меня есть" in it })
        assertTrue(sourceTexts.any { "Мы видим" in it })
        assertTrue(session.exercises.any { it.sourceText.trim().endsWith("?") })
        assertTrue(session.exercises.any { "ли" in it.correctAnswerWords })
    }

    @Test
    fun `lesson 10 session builds time routine exercises`() {
        val session = repository.getLessonSession(10)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.any { "понедельник" in it || "вторник" in it || "среду" in it || "четверг" in it || "пятницу" in it })
        assertTrue(sourceTexts.any { "до пяти" in it || "до трёх" in it || "до двух" in it })
        assertTrue(sourceTexts.any { it.startsWith("Когда ") || it.startsWith("Во сколько ") })
        assertTrue(bgWords.any { it in setOf("в", "във", "след", "преди", "от", "до", "сутрин", "вечер") })
        assertTrue(bgWords.any { it in setOf("закусвам", "вечерям", "обядвам", "чета", "отивам", "прибирам", "работя") })
        assertTrue(bgWords.contains("вкъщи"))
    }

    private fun isValidExercise(exercise: LessonExercise): Boolean {
        assertTrue(exercise.sourceText.isNotBlank())
        assertTrue(exercise.instruction.isNotBlank())
        assertFalse(exercise.correctAnswerWords.isEmpty())
        assertEquals(8, exercise.availableWords.size)
        assertTrue(exercise.correctAnswerWords.distinct().all { it in exercise.availableWords })
        return true
    }

    private fun loadLessonSessionAssets(): LessonSessionAssets {
        val path = resolveAssetPath("lesson_session_content.json")
        return json.decodeFromString(String(Files.readAllBytes(path), StandardCharsets.UTF_8))
    }

    private fun loadTextbookExerciseSet(lessonId: Int): TextbookLessonExerciseSetAsset {
        val path = resolveAssetPath("textbook_exercises_lesson$lessonId.json")
        return json.decodeFromString(String(Files.readAllBytes(path), StandardCharsets.UTF_8))
    }

    private fun resolveAssetPath(fileName: String): Path {
        val directPath = Paths.get("src", "main", "assets", fileName)
        if (Files.exists(directPath)) {
            return directPath
        }

        val appPath = Paths.get("app", "src", "main", "assets", fileName)
        if (Files.exists(appPath)) {
            return appPath
        }

        return directPath
    }
}
