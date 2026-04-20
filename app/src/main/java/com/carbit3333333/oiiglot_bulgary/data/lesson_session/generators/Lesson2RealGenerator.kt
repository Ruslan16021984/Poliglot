package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson2RealGenerator {

    private val templates = listOf(
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} {place}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.PlaceBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} не {place}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.Fixed("не"),
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.PlaceBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} {place}?",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.PlaceBg,
                LessonRealSentenceGenerator.Token.Fixed("ли"),
                LessonRealSentenceGenerator.Token.VerbBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "Это {noun}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.Fixed("Това"),
                LessonRealSentenceGenerator.Token.Fixed("е"),
                LessonRealSentenceGenerator.Token.NounBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "Это не {noun}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.Fixed("Това"),
                LessonRealSentenceGenerator.Token.Fixed("не"),
                LessonRealSentenceGenerator.Token.Fixed("е"),
                LessonRealSentenceGenerator.Token.NounBg
            )
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "Это {noun}?",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.Fixed("Това"),
                LessonRealSentenceGenerator.Token.NounBg,
                LessonRealSentenceGenerator.Token.Fixed("ли"),
                LessonRealSentenceGenerator.Token.Fixed("е")
            )
        )
    )

    private val subjectForms = listOf(
        Triple("Аз", "Я", "съм"),
        Triple("Ти", "Ты", "си"),
        Triple("Той", "Он", "е"),
        Triple("Ние", "Мы", "сме"),
        Triple("Вие", "Вы", "сте"),
        Triple("Те", "Они", "са")
    )

    private val places = listOf(
        "вкъщи" to "дома",
        "тук" to "здесь",
        "в училище" to "в школе",
        "в града" to "в городе",
        "на работа" to "на работе",
        "лекар" to "врач",
        "учител" to "учитель",
        "приятел" to "друг"
    )

    private val nouns = listOf(
        "книга" to "книга",
        "кафе" to "кофе",
        "вода" to "вода",
        "хляб" to "хлеб",
        "телефон" to "телефон",
        "чай" to "чай",
        "сок" to "сок"
    )

    private val distractorPool = listOf(
        "Аз", "Ти", "Той", "Ние", "Вие", "Те",
        "съм", "си", "е", "сме", "сте", "са",
        "не", "ли", "Това",
        "вкъщи", "тук", "в училище", "в града", "на работа",
        "лекар", "учител", "приятел",
        "книга", "кафе", "вода", "хляб", "телефон", "чай", "сок"
    )

    fun generateExercises(): List<LessonExercise> {
        return (1..100).map { id ->
            generateExercise(id)
        }
    }

    private fun generateExercise(id: Int): LessonExercise {
        val template = templates.random()

        val subjectTriple = subjectForms.random()
        val subjectBg = subjectTriple.first
        val subjectRu = subjectTriple.second
        val verbBg = subjectTriple.third

        val placePair = places.random()
        val nounPair = nouns.random()

        val lexicon = LessonRealSentenceGenerator.Lexicon(
            subject = LessonRealSentenceGenerator.SubjectForms(
                bg = subjectBg,
                ru = subjectRu
            ),
            verb = LessonRealSentenceGenerator.VerbForms(
                bg = verbBg,
                ru = ""
            ),
            placeBg = placePair.first,
            placeRu = placePair.second,
            nounBg = nounPair.first,
            nounRu = nounPair.second
        )

        val hint = when {
            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("Това") } &&
                    template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ли") } ->
                "💡 вопрос: Това + слово + ли + е"

            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("Това") } ->
                "💡 это → Това е ..."

            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ли") } ->
                "💡 с \"съм\" вопрос часто строится так: слово + ли + съм"

            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("не") } ->
                "💡 отрицание: не + форма на \"съм\""

            else -> null
        }

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = template,
            lexicon = lexicon,
            distractorPool = distractorPool,
            totalWords = 8,
            hint = hint
        )
    }
}