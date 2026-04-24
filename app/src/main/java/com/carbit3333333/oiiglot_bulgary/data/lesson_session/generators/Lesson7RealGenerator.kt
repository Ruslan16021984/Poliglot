package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonTemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonExerciseLocale
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson7RealGenerator {

    internal fun generateExercises(
        templates: List<LessonTemplateAsset>,
        exerciseLocale: LessonExerciseLocale = LessonExerciseLocale.Russian,
    ): List<LessonExercise> {
        val distractorPool = buildDistractorPool(templates)
        return (1..100).map { id -> generateExercise(id, templates, distractorPool, exerciseLocale) }
    }

    private fun generateExercise(
        id: Int,
        templates: List<LessonTemplateAsset>,
        distractorPool: List<String>,
        exerciseLocale: LessonExerciseLocale,
    ): LessonExercise {
        val template = templates[(id - 1) % templates.size]

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = when (exerciseLocale) {
                    LessonExerciseLocale.Ukrainian -> template.uk ?: template.ru
                    LessonExerciseLocale.Russian -> template.ru
                },
                bgPattern = template.bgWords.map(LessonRealSentenceGenerator.Token::Fixed),
            ),
            lexicon = LessonRealSentenceGenerator.Lexicon(
                subject = LessonRealSentenceGenerator.SubjectForms(
                    bg = "Аз",
                    ru = "Я",
                ),
            ),
            distractorPool = distractorPool,
            totalWords = 8,
            hint = template.hint,
        )
    }

    private fun buildDistractorPool(templates: List<LessonTemplateAsset>): List<String> {
        return templates
            .flatMap { it.bgWords }
            .distinct()
    }
}
