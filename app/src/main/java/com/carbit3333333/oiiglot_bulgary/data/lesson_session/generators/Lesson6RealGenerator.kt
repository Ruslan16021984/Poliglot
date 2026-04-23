package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

object Lesson6RealGenerator {

    private val subjectForms = listOf(
        Triple("Аз", "Я", "съм"),
        Triple("Ти", "Ты", "си"),
        Triple("Той", "Он", "е"),
        Triple("Тя", "Она", "е"),
        Triple("То", "Оно", "е"),
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
        "в къщата" to "в доме",
        "в магазина" to "в магазине",
        "в офиса" to "в офисе",
        "при учителя" to "у учителя",
        "с колегата" to "с коллегой"
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

    fun generateExercises(): List<LessonExercise> {
        val distractorPool = buildDistractorPool()
        return (1..100).map { id -> generateExercise(id, distractorPool) }
    }

    private fun generateExercise(
        id: Int,
        distractorPool: List<String>
    ): LessonExercise {
        val template = templates[(id - 1) % templates.size]
        val (subjectBg, subjectRu, verbBg) = subjectForms[((id - 1) / templates.size) % subjectForms.size]
        val placeIndex = ((id - 1) * cycleStep(placePhrases.size)) % placePhrases.size
        val (placeBg, placeRu) = placePhrases[placeIndex]

        val hint = when {
            template.bgPattern.any { it == LessonRealSentenceGenerator.Token.Fixed("ли") } ->
                "💡 вопрос: место или слово + ли + форма на \"съм\""
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

    private fun buildDistractorPool(): List<String> {
        return buildList {
            addAll(subjectForms.map { it.first })
            addAll(subjectForms.map { it.third })
            addAll(listOf("не", "ли"))
            addAll(placePhrases.map { it.first })
        }.distinct()
    }

    private fun cycleStep(size: Int): Int {
        if (size <= 1) return 1

        return (2..size).firstOrNull { candidate ->
            greatestCommonDivisor(candidate, size) == 1
        } ?: 1
    }

    private tailrec fun greatestCommonDivisor(
        left: Int,
        right: Int
    ): Int {
        return if (right == 0) left else greatestCommonDivisor(right, left % right)
    }
}
