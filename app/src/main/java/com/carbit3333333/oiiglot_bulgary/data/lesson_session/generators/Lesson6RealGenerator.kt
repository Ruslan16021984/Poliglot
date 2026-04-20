package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson6RealGenerator {

    private val subjectForms = listOf(
        Triple("Аз", "Я", "съм"),
        Triple("Ти", "Ты", "си"),
        Triple("Той", "Он", "е"),
        Triple("Ние", "Мы", "сме"),
        Triple("Вие", "Вы", "сте"),
        Triple("Те", "Они", "са")
    )

    private val placePhrases = listOf(
        "в града" to "в городе",
        "в училище" to "в школе",
        "на работа" to "на работе",
        "при лекаря" to "у врача",
        "с приятеля" to "с другом",
        "в къщата" to "в доме"
    )

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
        )
    )

    private val distractorPool = listOf(
        "Аз", "Ти", "Той", "Ние", "Вие", "Те",
        "съм", "си", "е", "сме", "сте", "са",
        "не", "ли",
        "в града", "в училище", "на работа", "при лекаря", "с приятеля", "в къщата"
    )

    fun generateExercises(): List<LessonExercise> {
        return (1..60).map { id -> generateExercise(id) }
    }

    private fun generateExercise(id: Int): LessonExercise {
        val template = templates.random()
        val (subjectBg, subjectRu, verbBg) = subjectForms.random()
        val (placeBg, placeRu) = placePhrases.random()

        val hint = when {
            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ли") } ->
                "💡 вопрос: место + ли + съм"
            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("не") } ->
                "💡 отрицание: не + форма на \"съм\""
            else ->
                "💡 используй предлог + существительное"
        }

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = template,
            lexicon = LessonRealSentenceGenerator.Lexicon(
                subject = LessonRealSentenceGenerator.SubjectForms(
                    bg = subjectBg,
                    ru = subjectRu
                ),
                verb = LessonRealSentenceGenerator.VerbForms(
                    bg = verbBg,
                    ru = ""
                ),
                placeBg = placeBg,
                placeRu = placeRu
            ),
            distractorPool = distractorPool,
            totalWords = 8,
            hint = hint
        )
    }
}