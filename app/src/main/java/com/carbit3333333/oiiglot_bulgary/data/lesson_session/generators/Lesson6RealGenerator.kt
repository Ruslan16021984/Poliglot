package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson6RealGenerator {

    private val subjectForms = listOf(
        Quadruple("Аз", "Я", "Я", "съм"),
        Quadruple("Ти", "Ты", "Ти", "си"),
        Quadruple("Той", "Он", "Він", "е"),
        Quadruple("Тя", "Она", "Вона", "е"),
        Quadruple("То", "Оно", "Воно", "е"),
        Quadruple("Ние", "Мы", "Ми", "сме"),
        Quadruple("Вие", "Вы", "Ви", "сте"),
        Quadruple("Те", "Они", "Вони", "са"),
    )

    private data class PlacePhrase(
        val bg: String,
        val ru: String,
        val uk: String,
    )

    private val placePhrases = listOf(
        PlacePhrase("в града", "в городе", "в місті"),
        PlacePhrase("в училище", "в школе", "в школі"),
        PlacePhrase("на работа", "на работе", "на роботі"),
        PlacePhrase("при лекаря", "у врача", "у лікаря"),
        PlacePhrase("с приятеля", "с другом", "з другом"),
        PlacePhrase("в къщата", "в доме", "в будинку"),
        PlacePhrase("в магазина", "в магазине", "в магазині"),
        PlacePhrase("в офиса", "в офисе", "в офісі"),
        PlacePhrase("при учителя", "у учителя", "у вчителя"),
        PlacePhrase("с колегата", "с коллегой", "з колегою"),
    )

    private val templates = listOf(
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} {place}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.PlaceBg,
            ),
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} не {place}",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.Fixed("не"),
                LessonRealSentenceGenerator.Token.VerbBg,
                LessonRealSentenceGenerator.Token.PlaceBg,
            ),
        ),
        LessonRealSentenceGenerator.SentenceTemplate(
            ruPattern = "{subject} {place}?",
            bgPattern = listOf(
                LessonRealSentenceGenerator.Token.SubjectBg,
                LessonRealSentenceGenerator.Token.PlaceBg,
                LessonRealSentenceGenerator.Token.Fixed("ли"),
                LessonRealSentenceGenerator.Token.VerbBg,
            ),
        ),
    )

    fun generateExercises(
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    ): List<LessonExercise> {
        val distractorPool = buildDistractorPool()
        return (1..100).map { id -> generateExercise(id, distractorPool, exerciseLocale) }
    }

    private fun generateExercise(
        id: Int,
        distractorPool: List<String>,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val template = templates[(id - 1) % templates.size]
        val (subjectBg, subjectRu, subjectUk, verbBg) = subjectForms[((id - 1) / templates.size) % subjectForms.size]
        val place = placePhrases[((id - 1) * cycleStep(placePhrases.size)) % placePhrases.size]

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
                    ru = if (exerciseLocale == LessonExerciseLocale.Ukrainian) subjectUk else subjectRu,
                ),
                verb = LessonRealSentenceGenerator.VerbForms(
                    bg = verbBg,
                    ru = "",
                ),
                placeBg = place.bg,
                placeRu = if (exerciseLocale == LessonExerciseLocale.Ukrainian) place.uk else place.ru,
            ),
            sourceTextOverride = buildSourceText(
                template = template,
                subject = if (exerciseLocale == LessonExerciseLocale.Ukrainian) subjectUk else subjectRu,
                place = if (exerciseLocale == LessonExerciseLocale.Ukrainian) place.uk else place.ru,
            ),
            distractorPool = distractorPool,
            totalWords = 8,
            hint = hint,
        )
    }

    private fun buildSourceText(
        template: LessonRealSentenceGenerator.SentenceTemplate,
        subject: String,
        place: String,
    ): String {
        return when {
            template.ruPattern.contains(" не ") -> "$subject не $place"
            template.ruPattern.endsWith("?") -> "$subject $place?"
            else -> "$subject $place"
        }
    }

    private fun buildDistractorPool(): List<String> {
        return buildList {
            addAll(subjectForms.map { it.first })
            addAll(subjectForms.map { it.fourth })
            addAll(listOf("не", "ли"))
            addAll(placePhrases.map { it.bg })
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
        right: Int,
    ): Int {
        return if (right == 0) left else greatestCommonDivisor(right, left % right)
    }

    private data class Quadruple<A, B, C, D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D,
    )
}
