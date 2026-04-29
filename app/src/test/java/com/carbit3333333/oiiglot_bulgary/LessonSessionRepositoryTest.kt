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
    fun `lesson 2 session uses textbook breakfast and food source`() {
        val session = repository.getLessonSession(2)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.contains("Что это?"))
        assertTrue(sourceTexts.contains("Это баница."))
        assertTrue(sourceTexts.contains("Я люблю кофе."))
        assertTrue(sourceTexts.contains("Ты хочешь воду?"))
        assertTrue(bgWords.contains("Това"))
        assertTrue(bgWords.contains("обичам"))
        assertTrue(bgWords.contains("искам"))
        assertTrue(bgWords.any { it in setOf("кафе", "вода", "баница", "хляб", "чай", "сок") })
    }

    @Test
    fun `lesson 3 session uses textbook restaurant source`() {
        val session = repository.getLessonSession(3)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.contains("Свободно?"))
        assertTrue(sourceTexts.contains("Да, пожалуйста, проходите."))
        assertTrue(sourceTexts.contains("Один кофе, пожалуйста."))
        assertTrue(sourceTexts.contains("Можно счёт?"))
        assertTrue(bgWords.any { it in setOf("меню", "сметката", "кафе", "айрян", "супа", "мусака") })
        assertTrue(session.exercises.any { "ли" in it.correctAnswerWords })
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
    fun `lesson 5 session uses textbook city shopping source`() {
        val session = repository.getLessonSession(5)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.contains("Сколько стоит?"))
        assertTrue(sourceTexts.contains("Сколько стоят?"))
        assertTrue(sourceTexts.contains("Можно один килограмм картофеля?"))
        assertTrue(sourceTexts.contains("Картой или наличными?"))
        assertTrue(sourceTexts.contains("Это аптека."))
        assertTrue(bgWords.any { it in setOf("карта", "торбичка", "пари", "аптека", "музей", "лимони") })
    }

    @Test
    fun `lesson 2 and 6 sessions use textbook coverage`() {
        val lesson2 = repository.getLessonSession(2)
        val lesson6 = repository.getLessonSession(6)
        val lesson2Words = lesson2.exercises.flatMap { it.correctAnswerWords }.toSet()
        val lesson6Words = lesson6.exercises.flatMap { it.correctAnswerWords }.toSet()
        val lesson6Texts = lesson6.exercises.map { it.sourceText }

        assertTrue(lesson2Words.any { it in setOf("Тя", "То", "Ние", "Вие", "Те") })
        assertTrue(lesson6Texts.any { "бабушка" in it || "брат" in it || "дети" in it })
        assertTrue(lesson6Texts.any { "женат" in it || "семья" in it || "Вашего сына" in it })
        assertTrue(lesson6Words.any { it in setOf("семейство", "деца", "брат", "сестра", "баба", "синът", "женен") })
    }

    @Test
    fun `lesson 7 and 8 sessions use textbook weather and clothing content`() {
        val lesson7 = repository.getLessonSession(7)
        val lesson8 = repository.getLessonSession(8)

        assertEquals(100, lesson7.exercises.size)
        assertEquals(100, lesson8.exercises.size)
        assertTrue(lesson7.exercises.all(::isValidExercise))
        assertTrue(lesson8.exercises.all(::isValidExercise))
        assertTrue(lesson7.exercises.any { "Сегодня тепло." in it.sourceText || "Сегодня холодно." in it.sourceText })
        assertTrue(lesson7.exercises.any { "Который час?" in it.sourceText || "Сейчас пять часов." in it.sourceText })
        assertTrue(lesson7.exercises.any { it.correctAnswerWords.contains("понеделник") || it.correctAnswerWords.contains("вторник") })
        assertTrue(lesson8.exercises.any { "Это рубашка." in it.sourceText || "Это платье." in it.sourceText })
        assertTrue(lesson8.exercises.any { "Рубашка синяя." in it.sourceText || "Юбка чёрная." in it.sourceText })
        assertTrue(lesson8.exercises.any { it.correctAnswerWords.contains("обувки") || it.correctAnswerWords.contains("чорапи") })
    }

    @Test
    fun `lesson 9 session uses textbook house and furniture content`() {
        val session = repository.getLessonSession(9)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.any { "Это дом." in it || "Это квартира." in it })
        assertTrue(sourceTexts.any { "Это кухня." in it || "Это ванная." in it })
        assertTrue(sourceTexts.any { "Это стол." in it || "Это кровать." in it })
        assertTrue(sourceTexts.any { "Дом большой." in it || "Комната маленькая." in it })
        assertTrue(sourceTexts.any { it.trim().endsWith("?") })
        assertTrue(bgWords.any { it in setOf("къща", "апартамент", "кухня", "баня", "маса", "легло", "диван", "шкаф") })
    }

    @Test
    fun `lesson 10 session uses textbook transport content`() {
        val session = repository.getLessonSession(10)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.any { "Это автобус." in it || "Это поезд." in it })
        assertTrue(sourceTexts.any { "Где вокзал?" in it || "Где остановка?" in it })
        assertTrue(sourceTexts.any { "У Вас есть билет?" in it || "Я хочу один билет до Софии." in it })
        assertTrue(sourceTexts.any { "Я еду на автобусе." in it || "Я еду поездом." in it })
        assertTrue(sourceTexts.any { it.trim().endsWith("?") })
        assertTrue(bgWords.any { it in setOf("автобус", "трамвай", "влак", "метро", "такси", "билет", "гара", "спирка") })
    }

    @Test
    fun `lesson 11 session uses textbook daily routine content`() {
        val session = repository.getLessonSession(11)
        val sourceTexts = session.exercises.map { it.sourceText }
        val bgWords = session.exercises.flatMap { it.correctAnswerWords }.toSet()

        assertEquals(100, session.exercises.size)
        assertTrue(session.exercises.all(::isValidExercise))
        assertTrue(sourceTexts.any { "Утром я встаю рано." in it || "Утром я пью кофе." in it })
        assertTrue(sourceTexts.any { "После завтрака я работаю." in it || "Вечером я возвращаюсь домой." in it })
        assertTrue(sourceTexts.any { "Когда ты завтракаешь?" in it || "Когда ты ложишься?" in it })
        assertTrue(sourceTexts.any { it.trim().endsWith("?") })
        assertTrue(bgWords.any { it in setOf("сутрин", "ставам", "закусвам", "работя", "вечерям", "вкъщи", "лягам") })
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
