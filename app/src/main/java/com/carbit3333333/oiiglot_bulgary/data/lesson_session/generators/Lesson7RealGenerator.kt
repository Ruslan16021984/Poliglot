package com.carbit3333333.oiiglot_bulgary.data.lesson_session.generators

import com.carbit3333333.oiiglot_bulgary.data.lesson_session.LessonTemplateAsset
import com.carbit3333333.oiiglot_bulgary.data.lesson_session.support.LessonRealSentenceGenerator
import com.carbit3333333.oiiglot_bulgary.model.LessonExercise

internal object Lesson7RealGenerator {

    private val distractorPool = listOf(
        "Това", "е", "са",
        "Аз", "Ти", "Ние",
        "имам", "виждам", "виждаш", "взимам", "обичаме", "Давам",
        "ти",
        "моята", "моят", "твоята", "своя",
        "нашето", "нашите",
        "книга", "книгата", "приятел", "дете", "книги"
    )

    internal fun generateExercises(templates: List<LessonTemplateAsset>): List<LessonExercise> {
        return (1..60).map { id -> generateExercise(id, templates) }
    }

    private fun generateExercise(
        id: Int,
        templates: List<LessonTemplateAsset>
    ): LessonExercise {
        val template = templates.random()

        return LessonRealSentenceGenerator.buildExercise(
            id = id,
            template = LessonRealSentenceGenerator.SentenceTemplate(
                ruPattern = template.ru,
                bgPattern = template.bgWords.map { LessonRealSentenceGenerator.Token.Fixed(it) }
            ),
            lexicon = LessonRealSentenceGenerator.Lexicon(
                subject = LessonRealSentenceGenerator.SubjectForms(
                    bg = "Аз",
                    ru = "Я"
                )
            ),
            distractorPool = distractorPool,
            totalWords = 8,
            hint = template.hint
        )
    }
}
