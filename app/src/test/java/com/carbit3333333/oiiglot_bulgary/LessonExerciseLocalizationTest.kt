package com.carbit3333333.oiiglot_bulgary

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson10IntervalAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson10PhraseAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson10TemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson3VerbAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9NumberAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9ObjectAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.Lesson9TemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonFixedSentenceAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonTemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generateLesson3Exercise
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generateLesson4Exercise
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson10RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson1RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson2RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson5RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson6RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson7RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators.Lesson9RealGenerator
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.localizeLessonExercises
import com.carbit3333333.oiiglot_bulgary.model.Lesson4Item
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonExerciseLocalizationTest {

    @Test
    fun `ukrainian localization translates exercise source instruction and hint`() {
        val exercise = LessonExercise(
            id = 1,
            sourceText = "Мы любим кофе дома?",
            instruction = "Переведите предложение",
            correctAnswerWords = listOf("Ние", "обичаме", "ли", "кафе", "вкъщи"),
            availableWords = listOf("Ние", "обичаме", "ли", "кафе", "вкъщи", "Аз", "не", "да"),
            hint = "Вопрос: глагол + ли",
        )

        val localized = localizeLessonExercises(
            exercises = listOf(exercise),
            locale = LessonExerciseLocale.Ukrainian,
        ).single()

        assertEquals("Перекладіть речення", localized.instruction)
        assertEquals("Ми любимо каву вдома?", localized.sourceText)
        assertTrue(localized.hint?.contains("Питання") == true)
    }

    @Test
    fun `lesson 1 uses exact ukrainian source text when it exists`() {
        val exercise = Lesson1RealGenerator.generateExercises(
            fixedSentences = listOf(
                LessonFixedSentenceAsset(
                    ru = "Я смотрю фильм",
                    uk = "Я дивлюся фільм",
                    correctWords = listOf("Аз", "гледам", "филм"),
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("Я дивлюся фільм", exercise.sourceText)
    }

    @Test
    fun `lesson 2 uses exact ukrainian source text when locale is ukrainian`() {
        val exercise = Lesson2RealGenerator.generateExercises(
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("Я вдома", exercise.sourceText)
    }

    @Test
    fun `lesson 3 uses exact ukrainian source text when locale is ukrainian`() {
        val exercise = generateLesson3Exercise(
            id = 1,
            subjectRuMap = mapOf(
                "Аз" to "Я",
                "Ти" to "Ты",
                "Той" to "Он",
                "Ние" to "Мы",
            ),
            subjectUkMap = mapOf(
                "Аз" to "Я",
                "Ти" to "Ти",
                "Той" to "Він",
                "Ние" to "Ми",
            ),
            verbs = listOf(
                Lesson3VerbAsset(
                    past = mapOf(
                        "Аз" to "правих",
                        "Ти" to "прави",
                        "Той" to "прави",
                        "Ние" to "правихме",
                    ),
                    ruPast = mapOf(
                        "Аз" to "делал(а)",
                        "Ти" to "делал(а)",
                        "Той" to "делал",
                        "Ние" to "делали",
                    ),
                    ukPast = mapOf(
                        "Аз" to "робив(ла)",
                        "Ти" to "робив(ла)",
                        "Той" to "робив",
                        "Ние" to "робили",
                    ),
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        )

        assertEquals("Я робив(ла)", exercise.sourceText)
    }

    @Test
    fun `lesson 4 uses exact ukrainian source text when it exists`() {
        val exercise = generateLesson4Exercise(
            id = 1,
            items = listOf(
                Lesson4Item(
                    type = Lesson4Item.Type.NOUN,
                    ru = "я хочу эту книгу",
                    uk = "я хочу цю книгу",
                    correctWords = listOf("Аз", "искам", "книгата"),
                ),
                Lesson4Item(
                    type = Lesson4Item.Type.VERB,
                    ru = "мы любим читать",
                    uk = "ми любимо читати",
                    correctWords = listOf("Ние", "обичаме", "да", "чета"),
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        )

        assertEquals("я хочу цю книгу", exercise.sourceText)
    }

    @Test
    fun `lesson 5 uses exact ukrainian source text when locale is ukrainian`() {
        val exercise = Lesson5RealGenerator.generateExercises(
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("Я можу дивитися фільм", exercise.sourceText)
    }

    @Test
    fun `lesson 6 uses exact ukrainian source text when locale is ukrainian`() {
        val exercise = Lesson6RealGenerator.generateExercises(
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("Я в місті", exercise.sourceText)
    }

    @Test
    fun `lesson 7 uses exact ukrainian source text when it exists`() {
        val exercise = Lesson7RealGenerator.generateExercises(
            templates = listOf(
                LessonTemplateAsset(
                    ru = "Это моя книга",
                    uk = "Це моя книга",
                    bgWords = listOf("Това", "е", "моята", "книга"),
                ),
                LessonTemplateAsset(
                    ru = "Это мой друг",
                    uk = "Це мій друг",
                    bgWords = listOf("Това", "е", "моят", "приятел"),
                ),
                LessonTemplateAsset(
                    ru = "Это наша машина",
                    uk = "Це наша машина",
                    bgWords = listOf("Това", "е", "нашата", "кола"),
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("Це моя книга", exercise.sourceText)
    }

    @Test
    fun `lesson 9 uses exact ukrainian source text when it exists`() {
        val exercise = Lesson9RealGenerator.generateExercises(
            numbers = listOf(
                Lesson9NumberAsset(
                    value = 1,
                    bgMasculine = "един",
                    bgFeminine = "една",
                    bgNeuter = "едно",
                    ruMasculine = "один",
                    ruFeminine = "одну",
                    ruNeuter = "одно",
                    ukMasculine = "один",
                    ukFeminine = "одну",
                    ukNeuter = "одне",
                ),
            ),
            objects = listOf(
                Lesson9ObjectAsset(
                    gender = "masculine",
                    singular = "билет",
                    plural = "билета",
                    countForm = "билет",
                    ruSingular = "билет",
                    ruPlural = "билета",
                    ruMany = "билетов",
                    ukSingular = "квиток",
                    ukPlural = "квитки",
                    ukMany = "квитків",
                ),
            ),
            templates = listOf(
                Lesson9TemplateAsset(
                    ruTokens = listOf("У", "меня", "есть", "{num}", "{object}"),
                    ukTokens = listOf("У", "мене", "є", "{num}", "{object}"),
                    bgTokens = listOf("Аз", "имам", "{num}", "{object}"),
                ),
                Lesson9TemplateAsset(
                    ruTokens = listOf("Я", "беру", "{num}", "{object}"),
                    ukTokens = listOf("Я", "беру", "{num}", "{object}"),
                    bgTokens = listOf("Аз", "взимам", "{num}", "{object}"),
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("У мене є один квиток", exercise.sourceText)
    }

    @Test
    fun `lesson 10 uses exact ukrainian source text when it exists`() {
        val exercise = Lesson10RealGenerator.generateExercises(
            timePhrases = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("В", "понедельник"),
                    ukTokens = listOf("У", "понеділок"),
                    bgTokens = listOf("В", "понеделник"),
                ),
            ),
            routineActions = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("пью", "кофе"),
                    ukTokens = listOf("п'ю", "каву"),
                    bgTokens = listOf("пия", "кафе"),
                ),
            ),
            intervals = listOf(
                Lesson10IntervalAsset(
                    ruFromTokens = listOf("с", "девяти"),
                    ruToTokens = listOf("до", "пяти"),
                    ukFromTokens = listOf("з", "дев'ятої"),
                    ukToTokens = listOf("до", "п'ятої"),
                    bgFromTokens = listOf("от", "девет"),
                    bgToTokens = listOf("до", "пет"),
                ),
            ),
            intervalActions = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("читаю"),
                    ukTokens = listOf("читаю"),
                    bgTokens = listOf("чета"),
                ),
            ),
            questionActions = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("ты", "работаешь"),
                    ukTokens = listOf("ти", "працюєш"),
                    bgTokens = listOf("работиш"),
                ),
            ),
            templates = listOf(
                Lesson10TemplateAsset(
                    ruTokens = listOf("{time}", "{action}"),
                    ukTokens = listOf("{time}", "{action}"),
                    bgTokens = listOf("{time}", "{action}"),
                ),
            ),
            intervalTemplates = listOf(
                Lesson10TemplateAsset(
                    ruTokens = listOf("{action}"),
                    ukTokens = listOf("{action}"),
                    bgTokens = listOf("{action}"),
                ),
            ),
            questionTemplates = listOf(
                Lesson10TemplateAsset(
                    ruTokens = listOf("Когда", "{action}"),
                    ukTokens = listOf("Коли", "{action}"),
                    bgTokens = listOf("Кога", "{action}"),
                    isQuestion = true,
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("У понеділок п'ю каву", exercise.sourceText)
    }

    @Test
    fun `lesson 10 fallback ukrainian time phrases sound natural`() {
        val exercise = Lesson10RealGenerator.generateExercises(
            timePhrases = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("В", "два", "часа"),
                    bgTokens = listOf("В", "два", "часа"),
                ),
            ),
            routineActions = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("работаю"),
                    bgTokens = listOf("работя"),
                ),
            ),
            intervals = listOf(
                Lesson10IntervalAsset(
                    ruFromTokens = listOf("с", "двух"),
                    ruToTokens = listOf("до", "трёх"),
                    bgFromTokens = listOf("от", "два"),
                    bgToTokens = listOf("до", "три"),
                ),
            ),
            intervalActions = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("читаю"),
                    bgTokens = listOf("чета"),
                ),
            ),
            questionActions = listOf(
                Lesson10PhraseAsset(
                    ruTokens = listOf("ты", "работаешь"),
                    bgTokens = listOf("работиш"),
                ),
            ),
            templates = listOf(
                Lesson10TemplateAsset(
                    ruTokens = listOf("{time}", "{action}"),
                    bgTokens = listOf("{time}", "{action}"),
                ),
            ),
            intervalTemplates = listOf(
                Lesson10TemplateAsset(
                    ruTokens = listOf("{action}", "{from}", "{to}"),
                    bgTokens = listOf("{action}", "{from}", "{to}"),
                ),
            ),
            questionTemplates = listOf(
                Lesson10TemplateAsset(
                    ruTokens = listOf("Когда", "{action}"),
                    bgTokens = listOf("Кога", "{action}"),
                    isQuestion = true,
                ),
            ),
            exerciseLocale = LessonExerciseLocale.Ukrainian,
        ).first()

        assertEquals("О другій годині працюю", exercise.sourceText)
    }
}
